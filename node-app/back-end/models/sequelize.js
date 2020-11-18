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

User.belongsTo(Role, {foreignKey: 'role_id'})

Post.hasMany(PostMessage, {foreignKey: 'post_id'})
PostMessage.belongsTo(Post, {foreignKey: 'post_id'})
PostMessage.belongsTo(User, {foreignKey: 'from_id'})
PostMessage.belongsTo(User, {foreignKey: 'to_id'})

sequelize.sync({force: true})
  .then(() => {
    console.log(`Database & tables created!`)
    Role.findAll().then(role => {
      if (role.length == 0) {
        initial()
      }
    })
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
}

module.exports = {
  User,
  Post,
  PostMessage,
  Role
}