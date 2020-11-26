const express = require('express');
const jwt = require('jsonwebtoken');
require('custom-env').env('dev')

const dotenv = require('dotenv');
dotenv.config();

const app = express();

var bodyParser = require('body-parser');

const {sequelize} = require('./models/sequelize')

const {User} = require('./models/sequelize')

const {Post} = require('./models/sequelize')

const {PostMessage} = require('./models/sequelize')

const {Role} = require('./models/sequelize')

// Body Parser
app.use(express.urlencoded({extended: false}));
app.use(bodyParser.json());
app.use((req, res, next) => {
  if (req.headers["access_token"]) {
    const accessToken = req.headers["access_token"];
    jwt.verify(accessToken, process.env.ACCESS_TOKEN_SECRET, (err, authData) => {
      if (err) {
        return res.status(401).json({error: 'JWT access token has expired, please login to obtain a new one'});
      } else {

        User.findOne({
          where: {
            user_id: authData.userId,
            access_token: accessToken
          }
        }).then(user => {
          if (user) {
            Role.findOne({
              where: {
                role_id: user.role_id
              }
            }).then(role => {
              if (role) {
                user.role = role.role_name;
                res.locals.loggedInUser = user;
                next();
              }
            }).catch(err => {
              return res.status(401).json({error: 'Server error'})
            })
          } else {
            return res.status(401).json({error: 'Unauthorized'})
          }
        }).catch(err => {
          return res.status(500).json({error: 'Server error'})
        })
      }
    });
  } else {
    next()
  }
});

app.use('/', require('./routes/routes'));

sequelize.sync({force: process.env.RECREATE_DATABASE === 'true' ? true: false})
  .then(() => {
    console.log(`Database & tables created!`)
    Role.findAll().then(role => {
      if (role.length == 0) {
        initial()
      }
    })
    app.on('databaseInitialized', () => {
      app.listen(5000, function() {
        console.log(`Express running on port 5000`);
        app.emit('serverStarted');
      });
    });
  })

async function initial() {

  await Role.bulkCreate([
    {
      role_name: "user"
    },
    {
      role_name: "moderator"
    },
    {
      role_name: "admin"
    }
  ])

  await User.create({
    first_name: 'User',
    last_name: 'One',
    email: 'user@one.com',
    password: '123456',
    active: true,
    role_id: 1
  });

  await User.create({
    first_name: 'User',
    last_name: 'Two',
    email: 'user@two.com',
    password: '123456',
    active: true,
    role_id: 3
  });

  await User.create({
    first_name: 'User',
    last_name: 'Three',
    email: 'user@three.com',
    password: '123456',
    active: true,
    role_id: 1
  });

  await User.create({
    first_name: 'User',
    last_name: 'Four',
    email: 'user@four.com',
    password: '123456',
    active: true,
    role_id: 1
  });

  await User.create({
    first_name: 'User',
    last_name: 'Five',
    email: 'user@five.com',
    password: '123456',
    active: true,
    role_id: 1
  });

  await Post.bulkCreate([
    {
      user_id: 1,
      content: 'XBox games for sale',
      active: true,
    },
    {
      user_id: 2,
      content: 'Playstation games for sale',
      active: true,
    }
  ])

  await PostMessage.bulkCreate([
    {
      post_id: 1,
      from_id: 2,
      to_id: 1,
      message: 'Is price negotiable?'
    },
    {
      post_id: 1,
      from_id: 1,
      to_id: 2,
      message: 'Yes. What\'s your offer?'
    },
    {
      post_id: 1,
      from_id: 3,
      to_id: 1,
      message: 'Still available?'
    },
    {
      post_id: 1,
      from_id: 1,
      to_id: 3,
      message: 'Yes.'
    }
  ])
  app.emit('databaseInitialized');
}

module.exports = app;
