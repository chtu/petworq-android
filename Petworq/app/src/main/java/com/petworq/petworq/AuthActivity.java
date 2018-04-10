package com.petworq.petworq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    public static final int RC_SIGN_IN = 123;
    public static final int RC_STORE_USER_INFO = 222;


    private List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
    );


    private TextView mSignInStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mSignInStatusTextView = (TextView) findViewById(R.id.sign_in_status_textview);


        if (!AuthUtil.userIsSignedIn()) {
            // Start the Firebase Auth Activity.
            // Update the status text views in onActivityResult
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(mProviders)
                            .build(),
                    RC_SIGN_IN
            );
        } else {
            AuthUtil.checkDatabaseForUserData(this);
        }
    }


    /**
     * Callback method for the signing in. Once it is successful, it will check to see if the user already
     * has a handle, and if they don't, then they will be taken to a screen where they can set up their handle.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Log.d(TAG, "Successfully signed in.");

                // Now that the user is successfully signed in, we have to make sure that they have a handle
                // and their data is in the database. We do this by retrieving the 'users' document under
                // this user's id. If it exists, the Activity will be killed off. If it doesn't, then
                // will go through the process of creating a handle and storing user info, then we will
                // finally kill it off once we address the result of StoreUserInfoActivity here.
                AuthUtil.checkDatabaseForUserData(this);
            } else {
                // Sign in failed, check response for error code
                Log.d(TAG, "onActivityResult:" + resultCode);
                finish();
            }
        } else if (requestCode == RC_STORE_USER_INFO ){
            // This the final step. We can kill off the Activity.
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}