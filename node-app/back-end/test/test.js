var expect = require('chai').expect;
const request = require('supertest');
const app = require('../app');
//var agent = request.agent(app);

before(done => {
  app.on('serverStarted', () => {
    done();
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
        expect(res.body).not.to.be.empty;
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
  var token = null;
  before(function (done) {
    request(app)
      .post('/api/login')
      .set('Accept', 'application/json')
      .set('Content-Type', 'application/json')
      .send({email: 'user@one.com', password: '123456'})
      .end(function (err, res) {
        token = res.body.token; // Or something
        done();
      });
  });

  it('Logout when already logged-in', function (done) {
    request(app)
      .get('/api/logout')
      .set('x-access-token', token)
      .end(function (err, res) {
        expect(res.body.message).to.equal('Logout successfully');
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

  describe('Create, Update Posts as a user', function () {
    var token = null;
    before(function (done) {
      request(app)
        .post('/api/login')
        .set('Accept', 'application/json')
        .set('Content-Type', 'application/json')
        .send({email: 'user@one.com', password: '123456'})
        .end(function (err, res) {
          token = res.body.token; // Or something
          done();
        });
    });

    it('Create post', function (done) {
      request(app)
        .post('/api/posts')
        .set('x-access-token', token)
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
        .set('x-access-token', token)
        .set('Accept', 'application/json')
        .send({content: 'XBox games for sale modified'})
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.message).to.equal('Post has been updated');
          done();
        })
    });

    it('Update other user post', function (done) {
      request(app)
        .put('/api/posts/2')
        .set('x-access-token', token)
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
        .set('x-access-token', token)
        .set('Accept', 'application/json')
        .expect('Content-Type', /json/)
        .expect(200)
        .end(function (err, res) {
          expect(res.body.message).to.equal('Post deleted successfully');
          done();
        })
    });

    it('Delete other user post', function (done) {
      request(app)
        .delete('/api/posts/2')
        .set('x-access-token', token)
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
    var token = null;
    before(function (done) {
      request(app)
        .post('/api/login')
        .set('Accept', 'application/json')
        .set('Content-Type', 'application/json')
        .send({email: 'user@two.com', password: '123456'})
        .end(function (err, res) {
          token = res.body.token; // Or something
          done();
        });
    });

    it('Update other user post', function (done) {
      request(app)
        .put('/api/posts/3')
        .set('x-access-token', token)
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
        .set('x-access-token', token)
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
