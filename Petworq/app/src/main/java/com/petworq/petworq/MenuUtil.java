package com.petworq.petworq;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MenuUtil {

    private static String TAG = "MenuUtil";

    public static void inflateMainMenu(AppCompatActivity context, Menu menu) {
        context.getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    public static void hideTest(Menu menu) {
        menu.findItem(R.id.create_petworq_menuitem).setVisible(false);
    }

    public static void setSignedInMenu(Menu menu) {
        menu.findItem(R.id.friends_menuitem).setVisible(true);
        menu.findItem(R.id.create_petworq_menuitem).setVisible(true);
        menu.findItem(R.id.settings_menuitem).setVisible(true);
        menu.findItem(R.id.sign_out_menuitem).setVisible(true);

        menu.findItem(R.id.sign_in_menuitem).setVisible(false);
    }

    public static void setSignedOutMenu(Menu menu) {
        menu.findItem(R.id.friends_menuitem).setVisible(false);
        menu.findItem(R.id.create_petworq_menuitem).setVisible(false);
        menu.findItem(R.id.settings_menuitem).setVisible(false);
        menu.findItem(R.id.sign_out_menuitem).setVisible(false);

        menu.findItem(R.id.sign_in_menuitem).setVisible(true);
    }


    public static void performMenuItemAction(AppCompatActivity context, MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.sign_out_menuitem):
                AuthUtil.signOut(context);
                break;
            case (R.id.sign_in_menuitem):
                AuthUtil.startSignInActivity(context);
                break;
        }

    }
}
