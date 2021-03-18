package com.muchlish.scan_ai.activity.entity;

public class Auth {
    String token;

    public Auth(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "token='" + token + '\'' +
                '}';
    }
}
