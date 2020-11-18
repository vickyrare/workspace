const Sequelize = require('sequelize');

module.exports = (sequelize, type) => {
  const Role = sequelize.define("roles", {
    role_id: {
      type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    role_name: {
      type: Sequelize.STRING
    },
  }, {underscored: true, timestamps: false});

  return Role
};