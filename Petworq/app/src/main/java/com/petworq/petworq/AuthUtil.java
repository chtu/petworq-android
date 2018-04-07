package com.petworq.petworq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    private Button mSignInButton;
    private Button mSignOutButton;
    private TextView mStatusTextView;

    private static FirebaseUser sUser;

    List<AuthUI.IdpConfig> providers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
        );

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mStatusTextView = (TextView) findViewById(R.id.status);

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);

        updateUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.sign_in_button):
                signIn();
                break;
            case (R.id.sign_out_button):
                signOut();
                break;
        }
    }

    private void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        );
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                // Sign in failed, check response for error code
                Log.d(TAG, "onActivityResult:" + resultCode);
            }
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI();
    }

    private void updateUI() {
        if (user == null) {
            mStatusTextView.setText("You are not signed in.");
            mSignInButton.setVisibility(View.VISIBLE);
            mSignOutButton.setVisibility(View.GONE);
        } else {
            mStatusTextView.setText(generateSignInMessage(user));
            mSignInButton.setVisibility(View.GONE);
            mSignOutButton.setVisibility(View.VISIBLE);
        }
    }

    private String generateSignInMessage(FirebaseUser user) {
        String outgoingMessage = "You are signed in as " + user.getDisplayName() + "\n";
        switch (user.getProviders().get(0)) {
            case ("google.com"):
                outgoingMessage += "via Google!";
                break;
            case ("password"):
                outgoingMessage += "via your email, " + user.getEmail();
                break;
            case ("facebook.com"):
                outgoingMessage += "via Facebook!";
                break;
        }
        return outgoingMessage;
    }

    public static FirebaseUser getUser() {
        return sUser;
    }

    public static void initUser() {
        sUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}