const mongoose = require('mongoose');
const { Schema } = mongoose;

const UserSchema = new Schema({
    username: {
        type: String,
        required: true,
    },
    password: {
        type: String,
        required: true
    },
    displayName: {
        type: String,
        required: true
    },
    profilePic: {
        type: String,
        required: true
    }
});


const MessageSchema = new Schema({
    id: {
        type: Number,
        integer: true
    },
    created: {
        type: Date,
        default: Date.now
    },
    sender: {
        username:{
            type:String,
            required:true
        },
    },
    content: {
        type: String,
        required: true
    }
});

const ChatSchema = new Schema({
    id: {
        type: Number,
        integer: true
    },
    users: [String],
    messages: [{
        type: MessageSchema,
        nullable: true
    }],
    lastMessage:{
        type:MessageSchema,
        nullable:true
    }
});


const Chat = mongoose.model('Chat', ChatSchema);
const Message = mongoose.model('Message', MessageSchema);
const User = mongoose.model('User', UserSchema);

module.exports = { Chat, Message, User };
