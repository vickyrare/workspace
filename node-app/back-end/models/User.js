const Sequelize = require('sequelize');
var bcrypt = require('bcrypt');

module.exports = (sequelize, type) => {
  const User = sequelize.define('users', {
    user_id: {
      type: type.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    first_name: {
      type: Sequelize.STRING,
      allowNull: false,
      notEmpty: true,
      validate: {
        len: {
          args: [3, 25],
          msg: 'The first_name should be between 3 and 50 characters'
        }
      }
    },
    last_name: {
      type: Sequelize.STRING,
      validate: {
        len: {
          args: [3, 25],
          msg: 'The last_name should be between 3 and 50 characters'
        }
      }
    },
    email: {
      type: Sequelize.STRING,
      validate: {
        isEmail: {
          args: true,
          msg: 'Valid email is required'
        },
        notEmpty: true
      }
    },
    password: {
      type: Sequelize.STRING,
      allowNull: false,
      notEmpty: false,
    },
    active: {
      type: Sequelize.BOOLEAN
    },
    access_token: {
      type: Sequelize.STRING,
      defaultValue: ''
    },
  }, {
    hooks: {
      afterValidate: function (user) {
        if (user.password != undefined)
          user.password = bcrypt.hashSync(user.password, parseInt(process.env.SALT_LENGTH))
      }
    },
    underscored: true,
  })

  return User
}