package com.petworq.petworq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.petworq.petworq.Authentication.AuthActivity;
import com.petworq.petworq.Authentication.SignInFragment;
import com.petworq.petworq.Toolbar.NotAuthenticatedToolbarFragment;
import com.petworq.petworq.Toolbar.ToolbarFragment;
import com.petworq.petworq.UtilityClasses.AuthUtil;
import com.petworq.petworq.UtilityClasses.DataUtil;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseAuth.AuthStateListener {

    private static final String TAG = "MainActivity";

    private static final boolean AUTOMATICALLY_SIGN_OUT = true;
    private static final boolean AUTOMATICALLY_SIGN_IN = true;

    private static final int RC_SIGN_IN = 111;
    private static final int RC_STORE_USER_INFO = 222;

    private FirebaseAuth mAuth;
    private DocumentReference mUserDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Set up the AuthStateListener
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        if (!AuthUtil.userIsSignedIn()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            NotAuthenticatedToolbarFragment notAuthToolbarFragment = new NotAuthenticatedToolbarFragment();
            SignInFragment signInFragment = new SignInFragment();


            fragmentTransaction.add(R.id.fragment_toolbar, notAuthToolbarFragment);
            fragmentTransaction.add(R.id.fragment_container, signInFragment);
            fragmentTransaction.commit();
        }

        // If DEBUG is true, the user will be signed out at the beginning of the session.
        if (AUTOMATICALLY_SIGN_OUT)
            AuthUtil.signOut(this);

        // If this user isn't signed in, automatically start the process.
        if (AUTOMATICALLY_SIGN_IN)
            startActivityForResult(new Intent(this, AuthActivity.class), RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
/*
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
        */
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (user == null) {
            NotAuthenticatedToolbarFragment newToolbarFragment = new NotAuthenticatedToolbarFragment();
            SignInFragment newContentFragment = new SignInFragment();

            fragmentTransaction.replace(R.id.fragment_toolbar, newToolbarFragment);
            fragmentTransaction.replace(R.id.fragment_container, newContentFragment);
        } else {
            ToolbarFragment newToolbarFragment = new ToolbarFragment();
            BaseOptionsFragment newContentFragment = new BaseOptionsFragment();

            fragmentTransaction.replace(R.id.fragment_toolbar, newToolbarFragment);
            fragmentTransaction.replace(R.id.fragment_container, newContentFragment);
        }

        fragmentTransaction.commit();
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

}


// TODO: Activity for friending others
// TODO: Activity for inviting others to your petwork
// TODO: Activity for setting up pet profiles
