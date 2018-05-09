package com.petworq.androidapp._main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.petworq.androidapp._main.AlarmManager.OnAlarmReceive;
import com.petworq.androidapp._main.navbar.NavigationBar;
import com.petworq.androidapp.di.app.AppComponent;
import com.petworq.androidapp.di.app.DaggerAppComponent;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features.authentication.AuthActivity;
import com.petworq.androidapp.R;
import com.petworq.androidapp.features.tasks.with_groups_joined.TasksController;
import com.petworq.androidapp.utilities.AppUtil;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.features.tasks.with_no_groups_joined.BaseOptionsController;
import com.petworq.androidapp.features.authentication.SignInController;
import com.petworq.androidapp._main.navbar.NavigationBarListener;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Module;

@Module
public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "MainActivity";

    public static final boolean AUTOMATICALLY_SIGN_OUT = false;
    public static final boolean AUTOMATICALLY_SIGN_IN = false;
    public static final boolean DEBUG = true;
    public static final boolean BACK_ALLOWED = false;
    public static final boolean CHECK_SHARED_PREFS = false;

    private static final int RC_SIGN_IN = 111;
    private static final int RC_STORE_USER_INFO = 222;

    private Router mRouter;
    private boolean mLastSignInStatus;
    private NavigationBarListener mNavigationBarListener;
    private AppTool mAppTool;

    private final static String ALARM_FILENAME = "petworq.android.charlietuttle.csc780.ALARM_STATUS";
    private final static String ALARM_STATUS_KEY = "alarmStatus";
    private final int ALARM_SET_VAL = 1;
    private final int DEFAULT_VAL = 0;

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
                mRouter.setRoot(RouterTransaction.with(new TasksController(mAppTool, null)));
                navBar.pushToPagesVisited(NavigationBar.TASKS);
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

        if (AuthUtil.userIsSignedIn()) {
            if (DEBUG || !DataUtil.userDataIsInitialized(this, AuthUtil.getUid())) {
                startActivityForResult(new Intent(this, AuthActivity.class), RC_SIGN_IN);
            }
        }

        setUpAlarmManager();
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
            mRouter.setRoot(RouterTransaction.with(new TasksController(mAppTool,null)));
            navBar.setVisibility(View.VISIBLE);
            mLastSignInStatus = true;
            navBar.pushToPagesVisited(NavigationBar.TASKS);
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

    @Override
    public void onBackPressed() {
        if (!mRouter.handleBack()) {
            super.onBackPressed();
        } else {
            navBar.popPagesVisited();
        }
    }


    // ALARM MANAGER METHODS
    // Sets up the alarm manager so that the tasks may update while the application is offline
    // It runs every hour
    private void setUpAlarmManager() {
        if (!alarmManagerIsSet()) {
            Log.d(TAG, "Setting up alarm manager.");
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(getBaseContext(), OnAlarmReceive.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                    AlarmManager.INTERVAL_HOUR, pendingIntent);

            updateAlarmManagerSet();
        }
    }

    // Checks our shared preferences to see if an alarm is already set.
    private boolean alarmManagerIsSet() {
        Log.d(TAG, "Checking alarm manager status.");
        SharedPreferences sharedPref = this.getSharedPreferences(ALARM_FILENAME, Context.MODE_PRIVATE);
        int status = sharedPref.getInt(ALARM_STATUS_KEY, DEFAULT_VAL);
        if (status == ALARM_SET_VAL)
            return true;
        else
            return false;
    }

    // Updates our shared preferences to show that the alarm is set.
    private void updateAlarmManagerSet() {
        Log.d(TAG, "Updating alarm manager status to true.");
        SharedPreferences sharedPref = this.getSharedPreferences(ALARM_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(ALARM_STATUS_KEY, ALARM_SET_VAL);
        editor.commit();
    }

}