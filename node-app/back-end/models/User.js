const Sequelize = require('sequelize');

module.exports = (sequelize, type) => {
  const User = sequelize.define('users', {
    user_id: {
      type: type.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    first_name: {
      type: Sequelize.STRING
    },
    last_name: {
      type: Sequelize.STRING
    },
    email: {
      type: Sequelize.STRING
    },
    password: {
      type: Sequelize.STRING
    },
    active: {
      type: Sequelize.BOOLEAN
    }
  }, { underscored: true})

  return User
}