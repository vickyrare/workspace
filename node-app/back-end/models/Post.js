const Sequelize = require('sequelize');

module.exports = (sequelize, type) => {
  const Post = sequelize.define('post', {
    post_id: {
      type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    content: {
      type: Sequelize.STRING
    },
    active: {
      type: Sequelize.BOOLEAN
    }
  }, { underscored: true})

  return Post
}