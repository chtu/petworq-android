package com.petworq.androidapp;

/**
 * Created by charlietuttle on 4/14/18.
 */

public interface User {
    // Unique ID
    public void setId(String newUniqueIdString);
    public String getId();

    // Handle
    public void setHandle(String newHandle);
    public void handleIsAvailable(String newHandle);
    public String getHandle();

    // Display Name
    public String setDisplayName(String newDisplayName);
    public String getDisplayName();
    public String validateDisplayName(String newDisplayName);

    public String getFirstName();
    public String getLastName();

    // Date methods
    public String getDateJoined();
    public String getLastDatetimeOnline();
}
