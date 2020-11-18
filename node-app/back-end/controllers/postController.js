const { Post } = require('../models/sequelize')
const { PostMessage } = require('../models/sequelize')
const { getPagination} = require('../utils/pagination');

const { roles } = require('../roles')

exports.createPost = (req, res) => {
  let { content } = req.body;
  let user_id = res.locals.loggedInUser.user_id
  Post.findOne({
    where: {
      user_id: user_id,
      content: content
    }
  }).then(post => {
    if (post) {
      res.status(409).json({
        error: 'Post with content already exist'
      });
    } else {
      let active = true;
      Post.create({
        user_id: user_id,
        content: content,
        active: active
      })
        .then(post => {
          res.json({
            post
          });
        })
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
      res.status(404).json({
        error: 'Post with post_id ' + post_id + ' not found'
      });
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.getPosts = (req, res) => {
  const { page, size } = req.query;
  const { limit, offset } = getPagination(page, size)
  Post.findAndCountAll({
    order: [
      ['post_id', 'DESC'],
    ],
    limit: limit,
    offset: offset,
  })
    .then(posts => {
      const response = getPagingData(posts, page, limit);
      res.send(response);
    })
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
            error: 'You don\'t have enough permission to perform this action'
          });
        }
      } else {
        res.status(404).json({
          error: 'Post with post_id ' + post_id + 'not found'
        });
      }
    }).catch(err => res.sendStatus(500, 'Server error'))
  } catch (error) {
    next(error)
  }
}

exports.getPostsForUser = (req, res) => {
  const { page, size } = req.query;
  const { limit, offset } = getPagination(page, size);
  const user_id = req.params.userid;
  Post.findAndCountAll({
    where: {
      user_id: user_id,
    },
    limit: limit,
    offset: offset,
  }).then(posts => {
    if (posts) {
      const response = getPagingData(posts, page, limit,);
      res.send(response);
    } else {
      res.status(404).json({
        error: 'No posts for user_id ' + user_id + ' were found'
      });
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
      res.status(404).json({
        error: 'Post with post_id ' + post_id + ' not found'
      });
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

exports.getPostMessagesByBuyer = (req, res) => {
  const post_id = req.params.postid;
  const to_id = req.params.buyerid;
  Post.findOne({
    where: {
      post_id: post_id
    }
  }).then(post => {
    if (post) {
      const from_id = post.user_id;
      PostMessage.findAll({
        where: {
          post_id: post_id,
          from_id: [from_id, to_id],
          to_id: [from_id, to_id],
        }
      }).then(post_messages => {
        if (post_messages.length > 0) {
          res.json(post_messages)
        } else {
          res.status(404).json({
            error: 'No message for post_id ' + post_id + ' were found'
          });
        }
      }).catch(err => res.sendStatus(500, 'Server error'))
    } else {
      res.status(404).json({
        error: 'Post with post_id ' + post_id + ' not found'
      });
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
        .catch(err => {
          res.status(404).json({
            error: 'Server error'
          })
        });
    } else {
      res.status(404).json({
        error: 'Post with post_id ' + post_id + ' not found'
      });
    }
  }).catch(err => res.sendStatus(500, 'Server error'))
}

const getPagingData = (data, page, limit) => {
  const { count: totalItems, rows: posts } = data;
  const currentPage = page ? +page : 0;
  const totalPages = Math.ceil(totalItems / limit);

  return { posts, totalItems, totalPages, currentPage };
};