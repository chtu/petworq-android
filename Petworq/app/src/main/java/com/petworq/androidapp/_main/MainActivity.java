package com.petworq.androidapp._main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.petworq.androidapp._main.navbar.NavigationBar;
import com.petworq.androidapp.di.app.AppComponent;
import com.petworq.androidapp.di.app.DaggerAppComponent;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features.authentication.AuthActivity;
import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AppUtil;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.features.tasks.with_no_groups_joined.BaseOptionsController;
import com.petworq.androidapp.features.authentication.SignInController;
import com.petworq.androidapp._main.navbar.NavigationBarListener;

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
    private AppTool mAppTool;

    @BindView(R.id.controller_container)
    ViewGroup container;

    @BindView(R.id.toolbar)
    NavigationBar navBar;

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
        mAppTool = appComponent.getAppTool();

        mAppTool.setContext(this);
        mAppTool.setNavBar(navBar);

        if (!mRouter.hasRootController()) {
            if (!AuthUtil.userIsSignedIn()) {
                mRouter.setRoot(RouterTransaction.with(new SignInController(mAppTool,null)));
                mLastSignInStatus = false;
            } else {
                mRouter.setRoot(RouterTransaction.with(new BaseOptionsController(mAppTool, null)));
                navBar.pushToBackStack(NavigationBar.DEFAULT_PAGE);
                mLastSignInStatus = true;
                navBar.setVisibility(View.VISIBLE);
            }
        }

        setSupportActionBar(navBar);

        mNavigationBarListener = new NavigationBarListener(this, mRouter, mAppTool);
        navBar.setOnMenuItemClickListener(mNavigationBarListener);

        // Set up the AuthStateListener
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

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
            mRouter.setRoot(RouterTransaction.with(new BaseOptionsController(mAppTool,null)));
            navBar.setVisibility(View.VISIBLE);
            mLastSignInStatus = true;
            navBar.pushToBackStack(NavigationBar.DEFAULT_PAGE);
        }
        if (!AuthUtil.userIsSignedIn() && mLastSignInStatus) {
            mRouter.setRoot(RouterTransaction.with(new SignInController(mAppTool,null)));
            navBar.setVisibility(View.GONE);
            mLastSignInStatus = false;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            mRouter.setRoot(RouterTransaction.with(new SignInController(mAppTool,null)));
            navBar.setVisibility(View.GONE);
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
        } else {
            navBar.popBackStack();
        }
    }

}


// TODO: Activity for friending others
// TODO: Activity for inviting others to your petwork
// TODO: Activity for setting up pet profiles
