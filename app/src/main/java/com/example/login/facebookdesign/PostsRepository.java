//package com.example.login.facebookdesign;
//
//import java.util.List;
//
//public class PostsRepository {
//    private PostDao dao;
//    private PostListData postListData;
//    private PostAPI api;
//    public PostsRepository() {
//        LocalDatabase db = LocalDatabase.getInstance();
//        dao = db.postDao();
//        postListData = new PostListData();
//     api
//            = new PostAPI(postListData, dao);
//
//}
//
//
//        public LiveData
//            <List
//                    <Post>> getAll() {
//         return postListData;
//
//    }
//
//         public void add
//            (final Post post) {
//         api.add(post);
//
//    }
//
//        public void delete
//            (final Post post) {
//         api.delete(post);
//
//    }
//
//        public void reload() {
//         api.get();
//
//    }
//
//}
