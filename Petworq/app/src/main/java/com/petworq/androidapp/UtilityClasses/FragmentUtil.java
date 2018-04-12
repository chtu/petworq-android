package com.petworq.androidapp.UtilityClasses;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.petworq.androidapp.R;

public class FragmentUtil {

    public static void updateMainUi(Fragment toolbar, Fragment content, AppCompatActivity context) {

        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_toolbar, toolbar);
        fragmentTransaction.replace(R.id.fragment_container, content);

        fragmentTransaction.commit();

    }

}
