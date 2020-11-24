const Sequelize = require('sequelize')
const UserModel = require('./User')
const RoleModel = require('./Role')
const PostModel = require('./Post')
const PostMessageModel = require('./PostMessage')

const sequelize = new Sequelize(process.env.DB_NAME, process.env.DB_USER, process.env.DB_PASS, {
  host: process.env.DB_HOST,
  dialect: 'postgres',
  pool: {
    max: 10,
    min: 0,
    acquire: 30000,
    idle: 10000
  },
  logging: false
})

const User = UserModel(sequelize, Sequelize)
const Role = RoleModel(sequelize, Sequelize)
const Post = PostModel(sequelize, Sequelize)
const PostMessage = PostMessageModel(sequelize, Sequelize)

User.hasMany(Post, {foreignKey: 'user_id'});
Post.belongsTo(User, {foreignKey: 'user_id'});

User.belongsTo(Role, {foreignKey: 'role_id'})

Post.hasMany(PostMessage, {foreignKey: 'post_id'})
PostMessage.belongsTo(Post, {foreignKey: 'post_id'})
PostMessage.belongsTo(User, {foreignKey: 'from_id'})
PostMessage.belongsTo(User, {foreignKey: 'to_id'})

module.exports = {
  sequelize,
  User,
  Post,
  PostMessage,
  Role
}