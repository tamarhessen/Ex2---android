const { Chat, Message, User } = require('../models/chat.js');
const jwt = require('jsonwebtoken');

let chatId = 0;

async function getChats(username) {
    const chats = await Chat.find({users: username });
    const result = await Promise.all(chats.map(async (chat)=> {
        const otherUsername = await chat.users.filter((str)=>str!==username)[0];
        const otherUser = await User.findOne({username:otherUsername})
        const modified = {
            id: chat.id,
            user: {
                username: otherUser.username,
                displayName: otherUser.displayName,
                profilePic: otherUser.profilePic
            },
            lastMessage:chat.lastMessage
        }
        return modified;
    }))

    return result;
}

async function createChat(username, targetUsername) {
    if(username===targetUsername) {
        return null;
    }
    const user = await User.findOne({ username: username });
    if (!user) {
        return null;
    }
    const targetUser = await User.findOne({ username: targetUsername });
    if (!targetUser) {
        return null;
    }
    // Get the ID of the last saved chat
    const lastChat = await Chat.findOne({}, {}, { sort: { id: -1 } }).lean();
    const lastChatId = lastChat ? lastChat.id : 0;

    // Increment the ID by 1 for the new chat
    const newChatId = lastChatId + 1;

    const chat = new Chat({
        id: newChatId,
        users: [username,
        targetUsername],
        messages: [],
        lastMessage:null
    });
    await chat.save();
    const result = {
        id: chat.id,
        user: {
            username: targetUser.username,
            displayName: targetUser.displayName,
            profilePic: targetUser.profilePic
        }
    };
    return result;
}
async function getUsernamesById(chatId) {
    const chat = await Chat.findOne({ id: chatId })
    console.log("async chat: " + chat.users);
    return chat.users;
}
async function getChatById(chatId) {
    const chat = await Chat.findOne({ id: chatId })
        .populate('users', 'username displayName profilePic')
        .populate('messages', 'id created content');
    return chat;
}

async function deleteChat(chatId) {
    const chat = await Chat.findOneAndDelete({ id: chatId });
    if (chat === null) {
        console.log('No chat found with the specified id.');
    }
    return chat;
}

async function createMessage(chatId, messageData) {
    const { senderUsername, content } = messageData;
    const chat = await Chat.findOne({ id: chatId });
    if (!chat) {
        return null;
    }
    let id = 0;
    if(chat.messages.length !== 0) {
        id = chat.messages[0].id+1;
    }
    const message = new Message({ id: id, sender: {username:senderUsername}, content: content });
    chat.messages.unshift(message);
    chat.lastMessage = message;
    await chat.save();
    return message;
}

async function getMessagesByChatId(chatId) {
    const chat = await Chat.findOne({ id: chatId }).populate('messages');
    if(!chat) {
        return null;
    }

    return chat.messages;
}

async function getUserByUsername(username) {
    const user = await User.findOne({ username: username }, 'username displayName profilePic');
    return user || null;
}

async function registerUser(userData) {
    const { username } = userData;
    const existingUser = await User.findOne({ username : username });
    if (existingUser) {
        return null;
    }
    const user = new User(userData);
    await user.save();
    return user;
}

async function generateToken(user) {
    const { username, password } = user
    const existingUser = await User.findOne({ username : username, password: password });
    if (!existingUser) {
        return null;
    }
    const payload = { username: username };
    const secretKey = 'your-secret-key';

    return jwt.sign(payload, secretKey);
}

module.exports = {
    getChats,
    createChat,
    getChatById,
    getUsernamesById,
    deleteChat,
    createMessage,
    getMessagesByChatId,
    getUserByUsername,
    registerUser,
    generateToken
};
