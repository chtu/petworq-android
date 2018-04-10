package com.petworq.petworq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FriendsHubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_hub);

        MenuUtil.setUpToolbar(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AuthUtil.finishActivityIfSignedOut(this);
    }


    // Toolbar callback methods --------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuUtil.inflateMainMenu(this, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the user is signed in, the menu will appear
        if (AuthUtil.userIsSignedIn())
            MenuUtil.setSignedInMenu(menu);
        else
            MenuUtil.setSignedOutMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.

        MenuUtil.performMenuItemAction(this, item);

        return super.onOptionsItemSelected(item);
    }
}
