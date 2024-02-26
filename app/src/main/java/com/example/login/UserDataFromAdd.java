package com.example.login;

public class UserDataFromAdd {
    private int id;
    private User.UserNoPassword user;

    public UserDataFromAdd(int id, User.UserNoPassword user) {
        this.id = id;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User.UserNoPassword getUser() {
        return user;
    }

    public void setUser(User.UserNoPassword user) {
        this.user = user;
    }
}
