package com.petworq.petworq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mGreetingTextView;
    private Button mSignInButton;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.petworq_toolbar);
        this.setSupportActionBar(myToolbar);

        AuthUtil.refreshUser();
        mGreetingTextView = findViewById(R.id.greeting_textview);
        mSignInButton = findViewById(R.id.sign_in_button);

        mSignInButton.setOnClickListener(this);

        AuthUtil.startSignInActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AuthUtil.refreshUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.sign_in_button):
                AuthUtil.startSignInActivity(this);
                break;
        }
    }

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

        updateUI();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.

        MenuUtil.performMenuItemAction(this, item);
        updateUI();

        return super.onOptionsItemSelected(item);
    }

    public void updateUI() {
        AuthUtil.refreshUser();
        if (AuthUtil.userIsSignedIn()) {
            mGreetingTextView.setText("Welcome back, " + AuthUtil.getName());
            mSignInButton.setVisibility(View.GONE);
        } else {
            mGreetingTextView.setText(getString(R.string.greeting_message_to_not_signed_in) + "\n"
                    + getString(R.string.sign_in_message));
            mSignInButton.setVisibility(View.VISIBLE);
        }
    }
}
