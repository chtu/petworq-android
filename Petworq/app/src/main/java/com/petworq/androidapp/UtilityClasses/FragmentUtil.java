package com.petworq.androidapp.UtilityClasses;


import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.petworq.androidapp.ContentFragments.BaseOptionsFragment;
import com.petworq.androidapp.ContentFragments.FriendsFragment;
import com.petworq.androidapp.ContentFragments.MessagesFragment;
import com.petworq.androidapp.ContentFragments.SettingsFragment;
import com.petworq.androidapp.ContentFragments.TasksFragment;
import com.petworq.androidapp.R;

public class FragmentUtil {

    public static void updateMainUi(Fragment toolbar, Fragment content, AppCompatActivity context) {

        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_toolbar, toolbar);
        fragmentTransaction.replace(R.id.fragment_container, content);

        fragmentTransaction.commit();
    }

    public static void addToUi(Fragment toolbar, Fragment content, AppCompatActivity context) {

        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_toolbar, toolbar);
        fragmentTransaction.add(R.id.fragment_container, content);

        fragmentTransaction.commit();
    }


    public static void updateToolBar(Fragment toolbar, AppCompatActivity context) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_toolbar, toolbar);

        fragmentTransaction.commit();
    }

    public static boolean isMainFragment(Fragment fragment) {
        if (fragment instanceof FriendsFragment
                || fragment instanceof MessagesFragment
                || fragment instanceof TasksFragment
                || fragment instanceof BaseOptionsFragment
                || fragment instanceof SettingsFragment)
            return true;
        else
            return false;
    }

}
