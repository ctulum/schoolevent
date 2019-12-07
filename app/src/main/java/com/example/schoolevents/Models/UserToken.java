package com.example.schoolevents.Models;

public class UserToken {
    private final String token;

    /**
     * Constructor.
     *
     * @param token the user token.
     */
    public UserToken(String token) {
        this.token = token;
    }

    protected String getToken() {
        return token;
    }
}
