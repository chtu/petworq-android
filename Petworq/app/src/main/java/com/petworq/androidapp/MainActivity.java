package com.petworq.androidapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.petworq.androidapp.Authentication.AuthActivity;
import com.petworq.androidapp.Authentication.SignInFragment;
import com.petworq.androidapp.Toolbar.NotAuthenticatedToolbarFragment;
import com.petworq.androidapp.Toolbar.ToolbarFragment;
import com.petworq.androidapp.UtilityClasses.AuthUtil;
import com.petworq.androidapp.UtilityClasses.FragmentUtil;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "MainActivity";

    private static final boolean AUTOMATICALLY_SIGN_OUT = true;
    private static final boolean AUTOMATICALLY_SIGN_IN = true;
    private static final boolean DEBUG = false;

    private static final int RC_SIGN_IN = 111;
    private static final int RC_STORE_USER_INFO = 222;

    private FirebaseAuth mAuth;
    private DocumentReference mUserDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the AuthStateListener
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        initializePage();

        if (DEBUG) {
            // If DEBUG is true, the user will be signed out at the beginning of the session.
            if (AUTOMATICALLY_SIGN_OUT)
                AuthUtil.signOut(this);

            // If this user isn't signed in, automatically start the process.
            if (AUTOMATICALLY_SIGN_IN)
                startActivityForResult(new Intent(this, AuthActivity.class), RC_SIGN_IN);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AuthUtil.getUser() == null) {
            NotAuthenticatedToolbarFragment newToolbarFragment = new NotAuthenticatedToolbarFragment();
            SignInFragment newContentFragment = new SignInFragment();

            FragmentUtil.updateMainUi(newToolbarFragment, newContentFragment, this);
        } else {
            ToolbarFragment newToolbarFragment = new ToolbarFragment();
            BaseOptionsFragment newContentFragment = new BaseOptionsFragment();

            FragmentUtil.updateMainUi(newToolbarFragment, newContentFragment, this);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            NotAuthenticatedToolbarFragment newToolbarFragment = new NotAuthenticatedToolbarFragment();
            SignInFragment newContentFragment = new SignInFragment();
            FragmentUtil.updateMainUi(newToolbarFragment, newContentFragment, this);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result of the AuthActivity
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "User successfully signed in.");
            } else {
                Log.d(TAG, "User backed out of the AuthActivity.");
            }
        }
    }
// TODO: Just removed onClick listener, see if that changes anything

    private void initializePage() {
        if (!AuthUtil.userIsSignedIn()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            NotAuthenticatedToolbarFragment notAuthToolbarFragment = new NotAuthenticatedToolbarFragment();
            SignInFragment signInFragment = new SignInFragment();

            fragmentTransaction.add(R.id.fragment_toolbar, notAuthToolbarFragment);
            fragmentTransaction.add(R.id.fragment_container, signInFragment);
            fragmentTransaction.commit();
        } else {
            startActivityForResult(new Intent(this, AuthActivity.class), RC_SIGN_IN);
        }
    }
}


// TODO: Activity for friending others
// TODO: Activity for inviting others to your petwork
// TODO: Activity for setting up pet profiles
