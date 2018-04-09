package com.petworq.petworq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity implements android.text.TextWatcher {

    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 123;
    public static final String SUCCESSFULLY_SIGNED_IN = "1";
    public static final String NEEDS_HANDLE = "2";

    private List<AuthUI.IdpConfig> mProviders = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
    );


    private TextView mSignInStatusNumTextView;
    private TextView mSignInStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mSignInStatusNumTextView = (TextView) findViewById(R.id.activity_auth_data_status);
        mSignInStatusTextView = (TextView) findViewById(R.id.sign_in_status_textview);

        mSignInStatusNumTextView.addTextChangedListener(this);


        // Start the Firebase Auth Activity.
        // Update the status text views in onActivityResult
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(mProviders)
                        .build(),
                RC_SIGN_IN
        );
    }


    // TextWatcher callbacks for when the status textviews are updated.
    // Once statuses are revealed, the appropriate actions are taken.
    @Override
    public void onTextChanged(CharSequence cs, int start, int count, int after) {
        switch (mSignInStatusNumTextView.getText().toString()) {
            case (NEEDS_HANDLE):
                startActivity(new Intent(this, StoreUserInfoActivity.class));
                break;
        }
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence cs, int start, int count, int after) { }
    @Override
    public void afterTextChanged(Editable s) {}




    /**
     * Callback method for the signing in. Once it is sucessful, it will check to see if the user already
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

                // Check the database if there already exists a document for this user, and if not,
                // update the TextViews to reflect the status
                DocumentReference userRef = FirebaseFirestore.getInstance().document("users/" + AuthUtil.getUser().getUid());
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            Log.d(TAG, "Username already taken.");
                            mSignInStatusNumTextView.setText(NEEDS_HANDLE);
                            mSignInStatusTextView.setText(getString(R.string.need_to_submit_handle));
                        } else {
                            Log.d(TAG, "Username is available.");
                            mSignInStatusNumTextView.setText(SUCCESSFULLY_SIGNED_IN);
                            mSignInStatusTextView.setText(getString(R.string.successfully_signed_in));
                        }
                    }
                });

            } else {
                // Sign in failed, check response for error code
                Log.d(TAG, "onActivityResult:" + resultCode);
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}