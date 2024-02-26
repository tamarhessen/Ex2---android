const chatService = require('../services/chat');
const {join} = require("path");

// Chat Controllers
async function getChats(req, res) {
    const chats = await chatService.getChats(req.user.username);
    res.json(chats);
}

async function createChat(req, res) {
    const chat = await chatService.createChat(req.user.username, req.body.username);
    if (!chat) {
        return res.status(404).json({ error: 'User not found' });
    }
    res.json(chat);
}

async function getChatById(req, res) {
    const chat = await chatService.getChatById(req.params.chatId);
    if (!chat) {
        return res.status(404).json({ error: 'Chat not found' });
    }
    res.json(chat);
}

async function deleteChat(req, res) {
    const chat = await chatService.deleteChat(req.params.chatId);
    if (!chat) {
        return res.status(404).json({ error: 'Chat not found' });
    }
    res.json(chat);
}

async function createMessage(req, res) {
    const message = await chatService.createMessage(req.params.chatId, {
        senderUsername: req.user.username,
        content: req.body.msg
    });
    if (!message) {
        return res.status(404).json({ error: 'Chat not found' });
    }
    res.json(message);
}

async function getMessagesByChatId(req, res) {
    const messages = await chatService.getMessagesByChatId(req.params.chatId);
    if(!messages) {
        return res.status(404)({error:'Chat not found'});
    }
    res.json(messages);
}

// Token Controller
async function generateToken(req, res) {
    const token = await chatService.generateToken(req.body);
    if (!token) {
        return res.status(404).json({ error: 'invalid username and or password' });
    }
    // res.json({ token });
    res.send(token);
}

// User Controller
async function getUserByUsername(req, res) {
    const user = await chatService.getUserByUsername(req.params.username);
    if (!user) {
        return res.status(404).json({ error: 'User not found' });
    }
    res.json(user);
}

async function registerUser(req, res) {
    const user = await chatService.registerUser(req.body);
    if (!user) {
        return res.status(400).json({ error: 'Username already exists' });
    }
    res.json(user);
}

async function redirectHome(req, res) {
    res.sendFile(join(__dirname,'..', 'public', 'index.html'));
}




module.exports = {
    getChats,
    createChat,
    getChatById,
    deleteChat,
    createMessage,
    getMessagesByChatId,
    generateToken,
    getUserByUsername,
    registerUser,
    redirectHome
};

