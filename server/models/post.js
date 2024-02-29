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
    created: {
        type: Date,
        default: Date.now
    },
    sender: {
        type: Schema.Types.ObjectId,
        ref: 'User', // Reference to the User model
        required: true
    },
    content: {
        type: String,
        required: true
    }
});

const PostSchema = new Schema({
    creator: {
        type: Schema.Types.ObjectId,
        ref: 'User', // Reference to the User model
        required: true
    },
    comments: [{
        type: Schema.Types.ObjectId,
        ref: 'Comment' // Reference to the Comment model
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
        type: Schema.Types.ObjectId,
        ref: 'User' // Reference to the User model
    }]
});

const Post = mongoose.model('Post', PostSchema);
const Comment = mongoose.model('Comment', CommentSchema);
const User = mongoose.model('User', UserSchema);
const Friends = mongoose.model('Friends', FriendsSchema);

module.exports = { Post, Comment, User, Friends };
