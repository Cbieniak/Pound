package com.bienprogramming.pound.app.POJO;

/**
 * Created by Christian on 6/06/2014.
 */
public class User {
    String name;

    String token;


    public User() {
    }

    public User(String name, String token) {
        this.name = name;
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
