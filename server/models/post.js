const mongoose = require('mongoose');
const { Schema } = mongoose;

const UserSchema = new Schema({
    username: {
        type: String,
        required: true,
        unique: true,
        minlength: 3,
        maxlength: 20
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


const CommentSchema = new Schema({
    id: {
        type: Number,
        required: true
    },
    created: {
        type: Date,
        default: Date.now
    },
    sender: {
        type: String,
        required: true
    },
    content: {
        type: String,
        required: true
    }
});

const PostSchema = new Schema({
    id: {
        type: Number,
        required: true
    },
    creator: {
        type: String,
        required: true
    },
    comments: [{
        type: CommentSchema,
        default: null
    }],
    postImg: {
        type: String,
        default: null
    },
    postText: {
        type: String,
        default: null
    },
    postLikes: {
        type: Number,
        default: 0
    },
    isPostLiked: {
        type: Boolean,
        required: true
    }
});

const FriendsSchema = new Schema({
    FriendList: [{
        type: String,
        nullable: true
    }]
})


const Post = mongoose.model('Post', PostSchema);
const Comment = mongoose.model('Comment', CommentSchema);
const User = mongoose.model('User', UserSchema);
const Friends = mongoose.model('Friends', FriendsSchema);

module.exports = { Post, Comment, User, Friends };
