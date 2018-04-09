package com.petworq.petworq;

/**
 * Created by charlietuttle on 4/7/18.
 */

public class User {

    private final static String TAG = "User";

    public String userId;
    public String firstName;
    public String lastName;

    public User() {

    }

    public User(String userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
