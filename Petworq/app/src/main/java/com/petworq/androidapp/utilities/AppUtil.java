package com.petworq.androidapp.utilities;

import android.support.annotation.NonNull;

import com.petworq.androidapp._main.MainActivity;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class AppUtil {

    private static MainActivity sMainActivity;

    public static void setMainActivity(@NonNull MainActivity mainActivity) {
        sMainActivity = mainActivity;
    }

    public static MainActivity getMainActivity() {
        return sMainActivity;
    }

}
