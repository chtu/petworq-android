package com.petworq.petworq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseAuth.AuthStateListener {

    private static final boolean AUTOMATICALLY_SIGN_OUT = false;
    private static final int RC_SIGN_IN = 111;
    private static final int RC_STORE_USER_INFO = 222;

    private TextView mGreetingTextView;
    private Button mSignInButton;
    private LinearLayout mPreSignInLayout;
    private LinearLayout mPostSignInLayoutNoGroups;

    private FirebaseAuth mAuth;
    private DocumentReference mUserDocRef;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        MenuUtil.setUpToolbar(this);

        mAuth = FirebaseAuth.getInstance();

        // Link up the views with our variables
        mGreetingTextView = (TextView) findViewById(R.id.greeting_textview);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mPreSignInLayout = (LinearLayout) findViewById(R.id.pre_sign_in_layout);
        mPostSignInLayoutNoGroups = (LinearLayout) findViewById(R.id.post_sign_in_layout_no_groups);

        // Set up the onClick listener for the sign in button
        mSignInButton.setOnClickListener(this);

        // Set up the AuthStateListener
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);


        // If DEBUG is true, the user will be signed out at the beginning of the session.
        if (AUTOMATICALLY_SIGN_OUT)
            AuthUtil.signOut(this);

        // If this user isn't signed in, automatically start the process.
        startActivityForResult(new Intent(this, AuthActivity.class), RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AuthUtil.userIsSignedIn()) {
            mUserDocRef = FirebaseFirestore.getInstance().document("users/" + AuthUtil.getUser().getUid());
            mUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Map<String, Object> data = document.getData();
                            long numGroups = (Long) data.get(DataUtil.USERS_FIELD_NUM_GROUPS_JOINED);
                            Log.d(TAG, "Retrived document data: " + numGroups);

                            if (numGroups == 0) {
                                switchToPostSignInNoGroupsLayout();
                                // TODO: Create a new activity for creating a new petworq
                            } else {
                                // TODO: Specify what to do when the user is a member of any groups
                            }

                        } else {
                            Log.d(TAG, "Document doesn't exist.");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {

            mGreetingTextView.setText("LOADING");
            mSignInButton.setVisibility(View.GONE);
        } else {
            mGreetingTextView.setText("You need to sign in to continue.");
            mSignInButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.sign_in_button):
                startActivityForResult(new Intent(this, AuthActivity.class), RC_SIGN_IN);
                break;
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

        switch (item.getItemId()) {
            case (R.id.sign_out_menuitem):
                AuthUtil.signOut(this);
                switchToPreSignInLayout();
                break;
            case (R.id.sign_in_menuitem):
                startActivity(new Intent(this, AuthActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchToPreSignInLayout() {
        mPreSignInLayout.setVisibility(View.VISIBLE);
        mPostSignInLayoutNoGroups.setVisibility(View.GONE);
    }

    public void switchToPostSignInNoGroupsLayout() {
        mPreSignInLayout.setVisibility(View.GONE);
        mPostSignInLayoutNoGroups.setVisibility(View.VISIBLE);
    }
}


// TODO: Activity for friending others
// TODO: Activity for inviting others to your petwork
// TODO: Activity for setting up pet profiles
