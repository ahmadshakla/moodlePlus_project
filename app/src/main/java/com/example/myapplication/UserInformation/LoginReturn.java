package com.example.myapplication.UserInformation;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * first level - get token from username and password
 */
public class LoginReturn {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}