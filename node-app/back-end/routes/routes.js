const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const postController = require('../controllers/postController');

router.post('/api/signup', userController.signup);
router.post('/api/login', userController.login);
router.get('/api/logout', userController.allowIfLoggedin, userController.logout);
router.get('/api/refresh', userController.refresh);

router.get('/api/users/:userId', userController.allowIfLoggedin, userController.getUser);
router.get('/api/users', userController.allowIfLoggedin, userController.grantAccess('readAny', 'users'), userController.getUsers);
router.put('/api/users/:userId', userController.allowIfLoggedin, userController.updateUser);

router.post('/api/posts', userController.allowIfLoggedin, postController.createPost);
router.get('/api/posts/:postId', postController.getPost);
router.get('/api/posts', postController.getPosts);
router.put('/api/posts/:postId', userController.allowIfLoggedin, postController.updatePost);
router.get('/api/posts/user/:userid', postController.getPostsForUser);
router.post('/api/posts/messages', userController.allowIfLoggedin, postController.createMessageForPost);
router.get('/api/posts/messages/:postid/:buyerid', userController.allowIfLoggedin, postController.getPostMessagesByBuyer);
router.delete('/api/posts/:postId', userController.allowIfLoggedin, postController.deletePost);

module.exports = router;