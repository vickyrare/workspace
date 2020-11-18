var express = require('express');
var router = express.Router();
var authController = require('./controllers/AuthController');
var helloController = require('./controllers/HelloController');
var authMiddleware = require('./middleware/AuthMiddleware');

router.post('/auth/register', authController.register);
router.post('/auth/login', authController.login);
router.get('/auth/logout', authMiddleware.Validate, authController.logout);
router.post('/auth/validate', authController.validate_token);
module.exports = router;

router.get('/hello', authMiddleware.Validate, helloController.simple_hello);