package com.example.stage2poche.entities;

import com.google.gson.annotations.SerializedName;

public class Auth {

    @SerializedName("login")
    public String login;

    @SerializedName("password")
    public String password;

    public Auth(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
