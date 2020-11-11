const { Post } = require('../models/sequelize')
const { PostMessage } = require('../models/sequelize')

const { roles } = require('../roles')

exports.createPost = (req, res) => {
  let { user_id, content } = req.body;
  Post.findOne({
    where: {
      user_id: user_id,
      content: content
    }
  }).then(post => {
    if (post) {
      res.sendStatus(409, 'Post with content already exist')
    } else {
      let active = true;
      Post.create({
        user_id: user_id,
        content: content,
        active: active
      })
        .then(post => res.sendStatus(200, post))
        .catch(err => res.sendStatus(500, 'Server error'))

    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.getPost = (req, res) => {
  const post_id = req.params.postId;
  Post.findOne({
    where: {
      post_id: post_id
    }
  }).then(post => {
    if (post) {
      res.json(post)
    } else {
      res.sendStatus(404, 'Post with post_id ' + post_id + 'not found')
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.getPosts = (req, res) => {
    Post.findAll()
      .then(posts => res.json(posts))
      .catch(err => res.sendStatus(403))
}

exports.updatePost = (req, res, next) => {
  try {
    const update = req.body
    const postId = req.params.postId;
    Post.findOne({
      where: {
        post_id: postId
      }
    }).then(post => {
      console.log('ok here')
      if (post) {
        const userId = post.user_id;
        const permission = roles.can(req.user.role).readOwn('posts');
        if (userId == res.locals.loggedInUser.user_id && permission.granted) {
          Post.update({
              content: update.content
            }, {
              where: {
                post_id: postId,
                user_id: res.locals.loggedInUser.user_id,
              }
            }
          ).then(post => res.status(200).json({
            post,
            message: 'Post has been updated'
          })).catch(err=> res.sendStatus(500, 'Server error'))
        } else {
          // resource is forbidden for this user/role
          res.status(401).json({
            error: "You don't have enough permission to perform this action"
          });
        }
      } else {
        res.sendStatus(404, 'Post with post_id ' + post_id + 'not found')
      }
    }).catch(err => res.sendStatus(500, 'Server error'))
  } catch (error) {
    next(error)
  }
}

exports.getPostsForUser = (req, res) => {
  const user_id = req.params.userid;
  Post.findAll({
    where: {
      user_id: user_id
    }
  }).then(posts => {
    if (posts) {
      res.json(posts)
    } else {
      res.sendStatus(404, 'No posts for user_id ' + user_id + 'were found')
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.createMessageForPost = (req, res) => {
  let from_id = res.locals.loggedInUser.user_id;
  let { post_id, to_id, message } = req.body;
  Post.findOne({
    where: {
      post_id: post_id
    }
  }).then(post => {
    if (post) {
      PostMessage.create({
        post_id: post_id,
        from_id: from_id,
        to_id: to_id,
        message: message
      })
        .then(post_message => res.sendStatus(200, post_message))
        .catch(err => res.sendStatus(500, 'Server error'))
    } else {
      res.sendStatus(404, 'Post with post_id ' + post_id + 'not found')
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.getPostMessagesBySeller=  (req, res) => {
  const post_id = req.params.postid;
  const from_id = req.params.sellerid;
  const to_id = req.params.buyerid;
  PostMessage.findAll({
    where: {
      post_id: post_id,
      from_id: [from_id, to_id],
      to_id: [from_id, to_id],
    }
  }).then(post_messages => {
    if (post_messages) {
      res.json(post_messages)
    } else {
      res.sendStatus(404, 'No message for post_id ' + post_id + 'were found')
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.deletePost = (req, res) => {
  const post_id = req.params.id;
  Post.findOne({
    where: {
      post_id: post_id
    }
  }).then(post => {
    if (post) {
      post.destroy()
        .then(post=> res.sendStatus(200))
        .catch(err => res.sendStatus(500, 'Server error'))
    } else {
      res.sendStatus(404, 'Post with id ' + post_id + 'not found')
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}
