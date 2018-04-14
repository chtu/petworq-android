package com.petworq.androidapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.petworq.androidapp.Authentication.AuthActivity;
import com.petworq.androidapp.UtilityClasses.AuthUtil;
import com.petworq.androidapp.controllers.SignInController;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Component;
import dagger.Module;
import dagger.android.AndroidInjector;

@Module
public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "MainActivity";

    public static final boolean AUTOMATICALLY_SIGN_OUT = true;
    public static final boolean AUTOMATICALLY_SIGN_IN = true;
    public static final boolean DEBUG = false;
    public static final boolean BACK_ALLOWED = false;

    private static final int RC_SIGN_IN = 111;
    private static final int RC_STORE_USER_INFO = 222;

    private Router mRouter;

    @BindView(R.id.controller_container)
    ViewGroup container;

    private FirebaseAuth mAuth;
    private DocumentReference mUserDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRouter = Conductor.attachRouter(this, container, savedInstanceState);

        if (!mRouter.hasRootController()) {
            mRouter.setRoot(RouterTransaction.with(new SignInController(null)));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sign_in);
        setSupportActionBar(toolbar);

        // Set up the AuthStateListener
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        //initializePage();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*

        if (AuthUtil.getUser() == null) {
            UnauthToolbarController newToolbarFragment = new UnauthToolbarController();
            SignInFragment newContentFragment = new SignInFragment();

            FragmentUtil.updateMainUi(newToolbarFragment, newContentFragment, this);
        }
        else {
            ToolbarFragment newToolbarFragment = new ToolbarFragment();
            FragmentUtil.updateToolBar(newToolbarFragment, this);
        }
        */
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        /*
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Log.d(TAG, "Changing layout to sign in page");
            UnauthToolbarController newToolbarFragment = new UnauthToolbarController();
            SignInFragment newContentFragment = new SignInFragment();
            FragmentUtil.updateMainUi(newToolbarFragment, newContentFragment, this);
        }
        */
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

/*
    private void initializePage() {
        if (!AuthUtil.userIsSignedIn()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            UnauthToolbarController notAuthToolbarFragment = new UnauthToolbarController();
            SignInFragment signInFragment = new SignInFragment();

            fragmentTransaction.add(R.id.fragment_toolbar, notAuthToolbarFragment);
            fragmentTransaction.add(R.id.fragment_container, signInFragment);
            fragmentTransaction.commit();
        } else {
            if (!DataUtil.userDataIsInitialized(this, AuthUtil.getUid())) {
                startActivityForResult(new Intent(this, AuthActivity.class), RC_SIGN_IN);
            }
        }
    }
    */

    @Override
    public void onBackPressed() {
        if (!mRouter.handleBack()) {
            super.onBackPressed();
        }
    }

}


// TODO: Activity for friending others
// TODO: Activity for inviting others to your petwork
// TODO: Activity for setting up pet profiles
