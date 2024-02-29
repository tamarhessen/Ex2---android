const express = require('express');
const router = express.Router();
const authenticateToken = require('../middleware/authenticateToken'); // Import the authenticateToken middleware

// Import post controller
const postController = require('../controllers/post');

// Routes for posts
router.get('/api/Posts', authenticateToken, postController.getPosts); // Route for getting posts
router.post('/api/Posts', authenticateToken, postController.createPost); // Route for creating a new post
router.get('/api/Posts/:postId', authenticateToken, postController.getPostById); // Route for getting a specific post by ID
router.delete('/api/Posts/:postId', authenticateToken, postController.deletePost); // Route for deleting a post
router.post('/api/Posts/:postId/Edit', authenticateToken, postController.editPost); // Route for editing a post
router.post('/api/Posts/:postId/Comments', authenticateToken, postController.createComment); // Route for creating a comment on a post
router.get('/api/Posts/:postId/Comments', authenticateToken, postController.getCommentsByPostId); // Route for getting comments on a post
router.delete('/api/Posts/:postId/Comments/:commentId', authenticateToken, postController.deleteComment); // Route for deleting a comment
router.post('/api/Posts/:postId/Comments/:commentId/Edit', authenticateToken, postController.editComment); // Route for editing a comment
router.get('/api/Posts/:postId/Like', authenticateToken, postController.likePost); // Route for liking a post

// Token route
router.post('/api/Tokens', postController.generateToken); // Route for generating a token

// User routes
router.get('/api/Users/:username', authenticateToken, postController.getUserByUsername); // Route for getting a user by username
router.post('/api/Users', postController.registerUser); // Route for registering a new user

// Other routes
router.get('/*', postController.redirectHome); // Catch-all route for redirecting to home

module.exports = router;
