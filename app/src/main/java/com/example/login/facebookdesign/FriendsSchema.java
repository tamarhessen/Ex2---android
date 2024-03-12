package com.example.login.facebookdesign;

import java.util.List;

public class FriendsSchema {
    private List<String> FriendList;
    private List<String> PendingList;

    public List<String> getFriendList() {
        return FriendList;
    }

    public void setFriendList(List<String> friendList) {
        this.FriendList = friendList;
    }

    public List<String> getPendingList() {
        return PendingList;
    }

    public void setPendingList(List<String> pendingList) {
        this.PendingList = pendingList;
    }
}
