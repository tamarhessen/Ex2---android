const chatController = require('../controllers/chat');

const express = require('express');
const router = express.Router();
const authenticateToken = require('../middleware/authenticateToken');

// Chats
router.get('/api/Chats', authenticateToken, chatController.getChats); ////////////////
router.post('/api/Chats', authenticateToken, chatController.createChat);
router.get('/api/Chats/:chatId', authenticateToken, chatController.getChatById);
router.delete('/api/Chats/:chatId', authenticateToken, chatController.deleteChat);
router.post('/api/Chats/:chatId/Messages', authenticateToken, chatController.createMessage);
router.get('/api/Chats/:chatId/Messages', authenticateToken, chatController.getMessagesByChatId); /////////////

// Tokens
router.post('/api/Tokens', chatController.generateToken);

// Users
router.get('/api/Users/:username', authenticateToken, chatController.getUserByUsername);
router.post('/api/Users', chatController.registerUser);

// Other routes
router.get('/*',chatController.redirectHome)

module.exports = router;

