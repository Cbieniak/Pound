package com.bienprogramming.pound.app.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Christian on 6/06/2014.
 */
public class User {
    private int id;

    String name;

    @SerializedName("authentication_token") String authenticationToken;


    public User() {
    }

    public User(String name, String authenticationToken) {
        this.name = name;
        this.authenticationToken = authenticationToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
