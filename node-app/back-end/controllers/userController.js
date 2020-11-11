const jwt = require('jsonwebtoken');
var bcrypt = require('bcrypt');
const saltRounds = 10;
const { User } = require('../models/sequelize')

// Add this to the top of the file
const { roles } = require('../roles')

exports.grantAccess = function(action, resource) {
  return async (req, res, next) => {
    try {
      const permission = roles.can(req.user.role)[action](resource);
      if (!permission.granted) {
        return res.status(401).json({
          error: "You don't have enough permission to perform this action"
        });
      }
      next()
    } catch (error) {
      next(error)
    }
  }
}

exports.allowIfLoggedin = async (req, res, next) => {
  try {
    const user = res.locals.loggedInUser;
    if (!user)
      return res.status(401).json({
        error: "You need to be logged in to access this route"
      });
    req.user = user;
    next();
  } catch (error) {
    next(error);
  }
}

exports.signup = (req, res) => {
  let { first_name, last_name, email, password } = req.body;
  User.findOne({
    where: {
      email: email
    }
  }).then(user => {
    if (user) {
      res.status(401).json({error: 'User with email already exist'});
    } else {
      let active = true;
      bcrypt.hash(password, saltRounds, function (err,   hash_password) {
        User.create({
          first_name,
          last_name,
          email,
          password: hash_password,
          active,
          role_id: 1
        })
          .then(user => res.sendStatus(200, user))
          .catch(err => res.sendStatus(500, 'Server error'))
      });
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.login = (req, res) => {
  let { email, password } = req.body;
  User.findOne({
    where: {
      email: email
    }
  }).then(user => {
    if (user) {
      bcrypt.compare(req.body.password, user.password, function (err, result) {
        if (result == true) {
          jwt.sign({userId: user.user_id}, process.env.SECRET_KEY, {expiresIn: '30m'}, (err, token) => {
            res.json({
              first_name: user.first_name,
              email: user.email,
              last_name: user.last_name,
              role: 'user',
              token
            });
          });
        } else {
          res.sendStatus(401, 'Unauthorized')
        }
      });
    } else {
      res.sendStatus(401, 'Unauthorized')
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.getUser = (req, res) => {
    const userId = req.params.userId;
    const permission = roles.can(req.user.role).readOwn('users');
    if (userId == res.locals.loggedInUser.user_id && permission.granted) {
      User.findOne({
        where: {
          user_id: userId,
        },
        attributes: { exclude: ['password']},
      })
        .then(user => res.json(user))
        .catch(err => res.sendStatus(403))
    } else {
      // resource is forbidden for this user/role
      res.status(401).json({
        error: "You don't have enough permission to perform this action"
      });
    }
}

exports.getUsers = (req, res) => {
    User.findAll({
    attributes: { exclude: ['password']
    },})
    .then(users => res.json(users))
    .catch(err => res.sendStatus(403))
}

exports.updateUser = (req, res, next) => {
  try {
    const update = req.body
    const userId = req.params.userId;
    const permission = roles.can(req.user.role).readOwn('users');
    if (userId == res.locals.loggedInUser.user_id && permission.granted) {
      User.update({
          first_name: update.first_name,
          last_name: update.last_name,
        }, {
          where: {
            user_id: userId
          }
        }
      ).then(user => res.status(200).json({
        user,
        message: 'User has been updated'
      })).catch(err=> res.sendStatus(500, 'Server error'))
    } else {
      // resource is forbidden for this user/role
      res.status(401).json({
        error: "You don't have enough permission to perform this action"
      });
    }
  } catch (error) {
    next(error)
  }
}