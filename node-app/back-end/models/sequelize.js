const Sequelize = require('sequelize')
const UserModel = require('./User')
const RoleModel = require('./Role')
const PostModel = require('./Post')
const PostMessageModel = require('./PostMessage')

const dotenv = require('dotenv');

dotenv.config();

const sequelize = new Sequelize(process.env.DB_NAME, process.env.DB_USER, process.env.DB_PASS, {
  host: process.env.DB_HOST,
  dialect: 'postgres',
  pool: {
    max: 10,
    min: 0,
    acquire: 30000,
    idle: 10000
  }
})

const User = UserModel(sequelize, Sequelize)
const Role = RoleModel(sequelize, Sequelize)
const Post = PostModel(sequelize, Sequelize)
const PostMessage = PostMessageModel(sequelize, Sequelize)

User.hasMany(Post, {foreignKey: 'user_id'});
Post.belongsTo(User, {foreignKey: 'user_id'});

Role.hasOne(User, {foreignKey: 'role_id'})

Post.hasMany(PostMessage, {foreignKey: 'post_id'})
PostMessage.belongsTo(Post, {foreignKey: 'post_id'})
PostMessage.belongsTo(User, {foreignKey: 'from_id'})
PostMessage.belongsTo(User, {foreignKey: 'to_id'})

sequelize.sync({force: false})
  .then(() => {
    console.log(`Database & tables created!`)
    initial()
  })

function initial() {
  Role.create({
    role_name: "user"
  });

  Role.create({
    role_name: "moderator"
  });

  Role.create({
    role_name: "admin"
  });
}
module.exports = {
  User,
  Post,
  PostMessage,
  Role
}