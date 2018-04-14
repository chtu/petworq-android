package com.petworq.androidapp;

/**
 * Created by charlietuttle on 4/13/18.
 */

public class ExampleInterfaceImpl implements ExampleInterface {

    private String mName;

    ExampleInterfaceImpl() {
        mName = "Charlie Tartle";
    }

    public String getName() {
        return this.mName;
    }
}
