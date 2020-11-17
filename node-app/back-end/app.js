const express = require('express');
const jwt = require('jsonwebtoken');
require ('custom-env').env('dev')

// Database
const db = require('./config/database');

// Test DB
db.authenticate()
    .then(() => console.log('Database connected...'))
    .catch(err => console.log('Error: ' + err))

const app = express();

var bodyParser = require('body-parser');

const { User } = require('./models/sequelize')

const { Role } = require('./models/sequelize')

// Body Parser
app.use(express.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use((req, res, next) => {
  if (req.headers["x-access-token"]) {
    const accessToken = req.headers["x-access-token"];

    jwt.verify(accessToken, process.env.SECRET_KEY, (err, authData) => {
      if (err) {
        return res.status(401).json({error: "JWT token has expired, please login to obtain a new one"});
      } else {

        User.findOne({
          where: {
            user_id: authData.userId
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
              return res.sendStatus(500, 'Server error')
            })
          } else {
            return res.sendStatus(401, 'Unauthorized')
          }
        }).catch(err => {
          return res.sendStatus(500, 'Server error')
        })
      }
    });
  } else {
      next()
  }
});

app.use('/', require('./routes/routes'));

app.listen(5000, () => console.log('Server started on port 5000'));