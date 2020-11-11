const Sequelize = require('sequelize');

module.exports = (sequelize, type) => {
  const PostMessage = sequelize.define('post_message', {
    message_id: {
      type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    message: {
      type: Sequelize.STRING
    }
  }, { underscored: true})
  return PostMessage
}