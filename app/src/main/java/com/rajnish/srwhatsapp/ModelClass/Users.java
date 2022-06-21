package com.rajnish.srwhatsapp.ModelClass;

public class Users {

    String name;
    String profile;
    String userid;
    String userToken;

    public Users(String name, String profile, String userid, String userToken) {
        this.name = name;
        this.profile = profile;
        this.userid = userid;
        this.userToken = userToken;
    }

    public Users() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
