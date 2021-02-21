var expect = require('chai').expect;
const request = require('supertest');
const app = require('../app');

before(done => {
  app.on('serverStarted', () => {
    done();
  });
});

describe('Sign up', function () {
  it('Sign up with all the required fields', function (done) {
    request(app)
      .post('/api/signup')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({first_name: 'new', last_name: 'user', email: 'new@user.com', password: '123456'})
      .expect(200)
      .expect('Content-Type', /json/)
      .end(function (err, res) {
        expect(res.body.user_id).to.be.a('number')
        expect(res.body.first_name).to.equal('new');
        expect(res.body.last_name).to.equal('user');
        expect(res.body.email).to.equal('new@user.com');
        done();
      });
  });

  it('Sign up with invalid email', function (done) {
    request(app)
      .post('/api/signup')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({first_name: 'new', last_name: 'user', email: 'userwithinvalidemail', password: '123456'})
      .expect(200)
      .expect('Content-Type', /json/)
      .end(function (err, res) {
        expect(res.body.error).to.equal('Valid email is required')
        done();
      });
  });

  it('Sign up with missing required field', function (done) {
    request(app)
      .post('/api/signup')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({last_name: 'user', email: 'user@valid.com', password: '123456'})
      .expect(200)
      .expect('Content-Type', /json/)
      .end(function (err, res) {
        expect(res.body.error).to.equal('users.first_name cannot be null')
        done();
      });
  });

  it('Sign up using same email', function (done) {
    request(app)
      .post('/api/signup')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({first_name: 'new', last_name: 'user', email: 'new@user.com', password: '123456'})
      .expect(401)
      .expect('Content-Type', /json/)
      .end(function (err, res) {
        expect(res.body.error).to.equal('User with email already exist');
        done();
      });
  });
});

describe('Login', function () {
  it('Login with correct username password', function (done) {
    request(app)
      .post('/api/login')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({email: 'user@one.com', password: '123456'})
      .expect(200)
      .expect('Content-Type', /json/)
      .end(function (err, res) {
        expect(res.body.access_token).not.to.be.empty;
        expect(res.body.refresh_token).not.to.be.empty;
        expect(res.body).to.be.an('object');
        done();
      });
  });

  it('Login with incorrect username password', function (done) {
    request(app)
      .post('/api/login')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({email: 'user@one.com', password: '1234567'})
      .expect(401)
      .expect('Content-Type', /json/)
      .end(function (err, res) {
        expect(res.body.error).to.equal('Login failure');
        done();
      });
  });
});

describe('Logout', function () {
  var accessToken = null;
  before(function (done) {
    request(app)
      .post('/api/login')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({email: 'user@one.com', password: '123456'})
      .end(function (err, res) {
        accessToken = res.body.access_token;
        done();
      });
  });

  it('Logout when already logged-in', function (done) {
    request(app)
      .get('/api/logout')
      .set('access_token', accessToken)
      .end(function (err, res) {
        expect(res.body.error).to.equal('You need to be logged in to access this route');
        done();
      });
  });

  it('Logout when not logged-in', function (done) {
    request(app)
      .get('/api/logout')
      .end(function (err, res) {
        expect(res.body.error).to.equal('You need to be logged in to access this route');
        done();
      });
  });
});

describe('Refresh token', function () {
  var refreshToken = null;
  before(function (done) {
    request(app)
      .post('/api/login')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({email: 'user@one.com', password: '123456'})
      .end(function (err, res) {
        refreshToken = res.body.refresh_token;
        done();
      });
  });

  it('Use valid refresh token to get access token', function () {
    request(app)
      .get('/api/refresh')
      .set('refresh_token', refreshToken)
      .expect(200)
      .expect('Content-Type', /json/)
      .end(function (err, res) {
        expect(res.body.access_token).not.to.be.empty;
      });
  });

  it('Use invalid refresh token to get access token', function () {
    request(app)
      .get('/api/refresh')
      .set('refresh_token', 'invalid_refresh_token')
      .end(function (err, res) {
        expect(res.body.error).to.equal('JWT refresh token has expired, please login to obtain a new one');
      });
  });
});

describe('Posts', function () {
  it('Get all posts', function () {
    request(app)
      .get('/api/posts')
      .set('Accept', 'application/json')
      .expect('Content-Type', /json/)
      .expect(200)
      .expect(function (response) {
        expect(response.body).not.to.be.empty;
        expect(response.body).to.be.an('object');
      })
  });

  it('Get one post', function () {
    request(app)
      .get('/api/posts/1')
      .set('Accept', 'application/json')
      .expect('Content-Type', /json/)
      .expect(200)
      .expect(function (response) {
        expect(response.body).not.to.be.empty;
        expect(response.body).to.be.an('object');
      })
  });

  describe('Create, Update, Delete Posts as a user', function () {
    var accessToken = null;
    before(function (done) {
      request(app)
        .post('/api/login')
        .set('Accept', 'application/json')
        .set('Content-Type', 'application/json')
        .send({email: 'user@one.com', password: '123456'})
        .end(function (err, res) {
          accessToken = res.body.access_token;
          done();
        });
    });

    it('Create post', function (done) {
      request(app)
        .post('/api/posts')
        .set('Authorization', 'Bearer ' + accessToken)
        .set('Accept', 'application/json')
        .send({content: 'post content'})
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.post.content).to.equal('post content');
          done();
        });
    });

    it('Update own post', function (done) {
      request(app)
        .put('/api/posts/1')
        .set('Authorization', 'Bearer ' + accessToken)
        .set('Accept', 'application/json')
        .send({content: 'XBox games for sale modified'})
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.message).to.equal('Post has been updated');
          done();
        })
    });

    it('Cannot update other user post', function (done) {
      request(app)
        .put('/api/posts/2')
        .set('Authorization', 'Bearer ' + accessToken)
        .set('Accept', 'application/json')
        .send({content: 'Playstation games for sale modified'})
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.error).to.equal('You don\'t have enough permission to perform this action');
          done();
        })
    });

    it('Delete own post', function (done) {
      request(app)
        .delete('/api/posts/1')
        .set('Authorization', 'Bearer ' + accessToken)
        .set('Accept', 'application/json')
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.message).to.equal('Post deleted successfully');
          done();
        })
    });

    it('Cannot delete other user post', function (done) {
      request(app)
        .delete('/api/posts/2')
        .set('Authorization', 'Bearer ' + accessToken)
        .set('Accept', 'application/json')
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.error).to.equal('You don\'t have enough permission to perform this action');
          done();
        })
    });
  });

  describe('Update, Delete Posts as admin user', function () {
    var accessToken = null;
    before(function (done) {
      request(app)
        .post('/api/login')
        .set('Accept', 'application/json')
        .set('Content-Type', 'application/json')
        .send({email: 'user@two.com', password: '123456'})
        .end(function (err, res) {
          accessToken = res.body.access_token;
          done();
        });
    });

    it('Update other user post', function (done) {
      request(app)
        .put('/api/posts/3')
        .set('Authorization', 'Bearer ' + accessToken)
        .set('Accept', 'application/json')
        .send({content: 'Playstation games for sale modified'})
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.message).to.equal('Post has been updated');
          done();
        })
    });

    it('Delete other user post', function (done) {
      request(app)
        .delete('/api/posts/3')
        .set('Authorization', 'Bearer ' + accessToken)
        .set('Accept', 'application/json')
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.message).to.equal('Post deleted successfully');
          done();
        })
    });
  });
});
