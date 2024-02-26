const express = require('express');
const app = express();
const mongoose = require('mongoose');
const cors = require('cors');
const bodyParser = require('body-parser');
const customEnv = require('custom-env');
const admin = require('firebase-admin');
const request = require('sync-request')
const serviceAccount = require('./serviceKey.json'); // Path to your service account key
const { initializeApp } = require('firebase-admin/app');
const projectId = serviceAccount.project_id; // Extract project ID from the service account key
// const app2 = initializeApp();
// const myRefreshToken = admin.credential.cert(serviceAccount); // Get refresh token from OAuth2 flow

// initializeApp({
//     credential: refreshToken(myRefreshToken),
//     databaseURL: "https://whatsappdesign-8f293.firebaseio.com"
// });
// let res1 = request('POST',"https://securetoken.googleapis.com/v1/token?key=AIzaSyCE3Omjyo8Oqix-Uh04evpu9kIvw2Zs-FM" );
// console.log(res1);
// admin.auth().getUser()
// const myRefreshToken = '...myRefreshToken';
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://whatsappdesign-8f293.firebaseio.com",
});

// Start of part 3:
const http = require("http");
const { Server } = require('socket.io');
app.use(cors());

customEnv.env(process.env.NODE_ENV, './config'); // Load environment variables from a custom file

const server = http.createServer(app);
const io = new Server(server, {
    cors: {
        origin: "http://localhost:5000",
        methods: ["GET", "POST"]
    }
})

// Initialize an empty object to store the username and socket ID pairs
const userSocketMap = {};
// Function to add a new pair of username and socket ID
function addUserSocket(username, socketId) {
    userSocketMap[username] = socketId;
}

// Function to get the socket ID for a given username
function getSocketId(username) {
    if (userSocketMap[username]) {
        return userSocketMap[username];
    } else {
        return null;
    }
}
// Function to remove a pair based on the socket ID
function removeUserSocketBySocketId(socketId) {
    for (const username in userSocketMap) {
        if (userSocketMap[username] === socketId) {
            delete userSocketMap[username];
            break; // Remove the first matching entry and exit the loop
        }
    }
}

function getSocketById(socketId) {
    const socket = io.sockets.sockets.get(socketId);
    return socket;
}
// Function to get the username for a given socket ID
function getUsernameBySocketId(socketId) {
    for (const username in userSocketMap) {
        if (userSocketMap[username] === socketId) {
            return username;
        }
    }
    // If no username is found for the provided socket ID
    return null;
}


const userPhoneMap = {};

// Function to add a new pair of username and socket ID
function addUserPhone(username, phone) {
    userPhoneMap[username] = phone;
}

// Function to get the socket ID for a given username
function getPhoneByUsername(username) {
    return userPhoneMap[username];
}
// Function to remove a pair based on the socket ID
function removePhoneByPhoneId(phone) {
    for (const username in userPhoneMap) {
        if (userPhoneMap[username] === phone) {
            delete userPhoneMap[username];
            break; // Remove the first matching entry and exit the loop
        }
    }
}

// function getPhoneByUsername(socketId) {
//     const socket = io.sockets.sockets.get(socketId);
//     return socket;
// }
// // Function to get the username for a given socket ID
// function getUsernameBySocketId(socketId) {
//     for (const username in userSocketMap) {
//         if (userSocketMap[username] === socketId) {
//             return username;
//         }
//     }
//     // If no username is found for the provided socket ID
//     return null;
// }


io.on("connection", (socket) => {
    console.log("connected: ", socket.id);
    socket.emit("connected", {socket: socket.id});
    console.log("sent: socket: " + socket.id);
    socket.on("logged_in", (data) => {
        console.log("hello");
        console.log("username: " + data.username);
        console.log("socket: " + socket.id);
        if (getSocketId(data.username) != null) {
            removeUserSocketBySocketId(getSocketId(data.username));
        }
        addUserSocket(data.username, socket.id);
    });
    socket.on("send_message", (data) => {

        let chatId = data.chat_id;
        console.log("chat id: " + data.chat_id);
        console.log("username: " + data.username);
        let username = data.username;
        const recipientSocket = getSocketId(username);
        console.log("data.socket: " + data.socket);
        console.log("recipientSocket: " + recipientSocket);
        if (recipientSocket) {
            const socket = getSocketById(recipientSocket)
            console.log("recipientSocket2: " + socket);
            socket.emit('receive_message', data);
        }

        //Send push notification to the recipient
        let firebase_token = getPhoneByUsername(username);
        console.log("firebase_token: ", firebase_token);
        if (firebase_token) {
            console.log("firebase_token: ", firebase_token);
            sendPushNotification(firebase_token, data.message).then();
        }
    });
    socket.on("using_phone", (data) => {
        console.log("adding phone");
        console.log("username: " + data.username);
        console.log("token: " + data.token);
        addUserPhone(data.username,data.token);
    })
    socket.on("message_received", () =>{
        console.log("hello world");
    });
    // Handle disconnection
    socket.on('disconnect', () => {
        console.log('A user disconnected:', socket.id);

        // Remove the client from the connected clients
        removeUserSocketBySocketId(socket.id);
    });
});

// Send push notification using Firebase Cloud Messaging (FCM)
// const admin = require('firebase-admin');

// Initialize the Firebase Admin SDK with your service account credentials
// const serviceAccount = require('/path/to/serviceAccountKey.json'); // Update with the path to your service account JSON file
// admin.initializeApp({
//     credential: admin.credential.cert(serviceAccount),
// });

async function sendPushNotification(recipient, message) {
    try {
        // Create the notification payload
        const payload = {
            notification: {
                title: 'New Message',
                body: message,
            },
        };

        // Send the notification to the recipient token
        const response = await admin.messaging().sendToDevice(recipient, payload);

        console.log('Push notification sent successfully:', response);
    } catch (error) {
        console.error('Error sending push notification:', error);
    }
}

// async function sendPushNotification(recipient, message) {
//     try {
//         // Create the notification payload
//         const payload = {
//             notification: {
//                 title: 'New Message',
//                 body: message,
//             },
//         };
//
//         // Send the notification to the recipient token
//         const response = await admin.messaging().sendToDevice(recipient, payload);
//
//         console.log('Push notification sent successfully:', response);
//     } catch (error) {
//         console.error('Error sending push notification:', error);
//     }
// }
server.listen(process.env.PORT_COMMUNICATION, () => {})

// Middleware
app.use(express.json()); // Parse JSON request bodies
app.use(bodyParser.urlencoded({ extended: true, limit:'5mb'})); // Parse URL-encoded request bodies
app.use(bodyParser.json({limit:'5mb'})); // Parse JSON request bodies
app.use(cors()); // Enable Cross-Origin Resource Sharing


// Connect to MongoDB
mongoose.connect(process.env.CONNECTION_STRING, { // Connect to MongoDB using the specified URL
    useNewUrlParser: true,
    useUnifiedTopology: true
})
    .then(() => {
        console.log('Connected to MongoDB');
    })
    .catch((err) => {
        console.error('Failed to connect to MongoDB', err);
    });

app.use(express.static('public')); // Serve static files from the 'public' directory

// Routes
const chatRoutes = require('./routes/chat');
const {getUsernamesById} = require("./services/chat");
const {refreshToken} = require("firebase-admin/app");
app.use('/', chatRoutes); // Mount the chat routes on the root path

// Start the server
app.listen(process.env.PORT_MONGO, () => { // Start the server and listen on port process.env.PORT
    console.log('Server started on port: ' + process.env.PORT_MONGO);
});
