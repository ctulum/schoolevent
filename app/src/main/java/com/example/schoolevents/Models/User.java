package com.example.schoolevents.Models;

public class User {

    private static String id;
    private static String email;
    private static int accountType;
    private static boolean status;
    private static String password;

    private static User user;

    public static void init(){
        if(user != null) return;

        user = new User();
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        User.id = id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public static int getAccountType() {
        return accountType;
    }

    public static void setAccountType(int accountType) {
        User.accountType = accountType;
    }

    public static boolean isStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        User.status = status;
    }
}
