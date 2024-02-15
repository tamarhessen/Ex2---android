# Ex2---android
## This project is the front end of the adinroid application of facebook.
### Made by: Tamar Hessen, Sapir Yanai, and Daniel Lifshitz.
## Run:
Build and run using ide
## adapters
### CommentsAdapter
Initializing RecyclerView and its adapter for comments.
### PostsListAdapter
Initializing RecyclerView and its adapter for posts.
## entities
### Posts
representing a social media post with attributes such as author, content,
likes, images, timestamps, and comments, along with methods for managing post data.
## CommentsActivity
This is where he comments are made
## CreateAccountActivity
This is all the logic of creating an account
## EditPostDialogFragment
The dialog for editing posts
## FeedActivity
class sets up a feed interface where users can view and interact with posts,
including options to navigate to different sections and create new posts/
## JsonParser
provides a method to parse JSON data representing posts, extracting relevant information
such as author, content, images, and likes, and then constructs Post objects to populate a list of posts.
## LoginActivity
 handles user authentication by verifying entered credentials against a list of predefined users
 and facilitates account creation or login accordingly, navigating to the feed upon successful authentication.
## MainActivity
initiates the application's entry point by applying any saved dark mode settings and then
starting the login activity, ensuring the main activity is not added to the back stack.
## Menu Activity
displays a menu screen with options for navigating to the home screen, logging out, and accessing settings,
while also displaying the user's profile picture and display name retrieved from user credentials.
## NewPostActivity
 allows users to create a new post by selecting an image from the gallery or taking a photo with the camera,
 entering text for the post, and then submitting the post.
## SettingsActivity
 provides functionality for users to toggle dark mode on or off. It also allows users to apply the selected
 mode immediately using the apply button.
## UserCredentials
This hase all the infomation of the users so that we can accsess them in the app.
