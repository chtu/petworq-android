package com.petworq.androidapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.petworq.androidapp.Authentication.AuthActivity;
import com.petworq.androidapp.UtilityClasses.AppUtil;
import com.petworq.androidapp.UtilityClasses.AuthUtil;
import com.petworq.androidapp.controllers.BaseOptionsController;
import com.petworq.androidapp.controllers.SignInController;
import com.petworq.androidapp.listeners.NavigationBarListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Module;

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
    private boolean mLastSignInStatus;
    private NavigationBarListener mNavigationBarListener;

    @BindView(R.id.controller_container)
    ViewGroup container;

    @BindView(R.id.toolbar)
    Toolbar navigationBar;

    private FirebaseAuth mAuth;
    private DocumentReference mUserDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtil.setMainActivity(this);
        ButterKnife.bind(this);


        mRouter = Conductor.attachRouter(this, container, savedInstanceState);

        AppComponent appComponent = DaggerAppComponent.create();
        AppTool appTool = appComponent.getAppTool();

        appTool.setContext(this);
        appTool.setToolbar(navigationBar);

        if (!mRouter.hasRootController()) {
            if (!AuthUtil.userIsSignedIn()) {
                mRouter.setRoot(RouterTransaction.with(new SignInController(null)));
                mLastSignInStatus = false;
            } else {
                mRouter.setRoot(RouterTransaction.with(new BaseOptionsController(appTool, null)));
                mLastSignInStatus = true;
                navigationBar.setVisibility(View.VISIBLE);
            }
        }

        setSupportActionBar(navigationBar);

        mNavigationBarListener = new NavigationBarListener(this, mRouter, appTool);
        navigationBar.setOnMenuItemClickListener(mNavigationBarListener);

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
        if (AuthUtil.userIsSignedIn() && !mLastSignInStatus) {
            mRouter.setRoot(RouterTransaction.with(new BaseOptionsController(null)));
            navigationBar.setVisibility(View.VISIBLE);
            mLastSignInStatus = true;
        }
        if (!AuthUtil.userIsSignedIn() && mLastSignInStatus) {
            mRouter.setRoot(RouterTransaction.with(new SignInController(null)));
            navigationBar.setVisibility(View.GONE);
            mLastSignInStatus = false;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            mRouter.setRoot(RouterTransaction.with(new SignInController(null)));
            navigationBar.setVisibility(View.GONE);
            mLastSignInStatus = false;
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

    private void initUi() {
    }



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
