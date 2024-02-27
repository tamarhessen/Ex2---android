package com.example.login.facebookdesign;

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

    public String getDisplayName() {
        return this.user.getDisplayName();
    }

    public String getProfilePic() {
        return this.user.getProfilePic().toString();
    }
}
