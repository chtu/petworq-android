package com.petworq.petworq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, android.text.TextWatcher {

    private static final boolean AUTOMATICALLY_SIGN_OUT = false;

    private TextView mGreetingTextView;
    private TextView mHandleStatusTextView;
    private Button mSignInButton;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.petworq_toolbar);
        this.setSupportActionBar(myToolbar);

        mGreetingTextView = (TextView) findViewById(R.id.greeting_textview);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mHandleStatusTextView = (TextView) findViewById(R.id.main_activity_data_status);

        mSignInButton.setOnClickListener(this);
        mHandleStatusTextView.addTextChangedListener(this);

        // If DEBUG is true, the user will be signed out at the beginning of the session.
        if (AUTOMATICALLY_SIGN_OUT)
            AuthUtil.signOut(this);

        // If this user isn't signed in, automatically start the process.
        if (!AuthUtil.userIsSignedIn()) {
            startActivity(new Intent(this, AuthActivity.class));
        }
        // If they are signed in, check to see if the document was created for them. If not, they probably
        // exited the StoreUserInfoActivity before they created a handle. This makes sure they complete this
        // process before they can continue.
        else {
            DocumentReference userRef = FirebaseFirestore.getInstance().document("users/" + AuthUtil.getUser().getUid());
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (!documentSnapshot.exists()) {
                        mHandleStatusTextView.setText(AuthActivity.NEEDS_HANDLE);
                    }
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.sign_in_button):
                startActivity(new Intent(this, AuthActivity.class));
                break;
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

        updateUI();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.

        MenuUtil.performMenuItemAction(this, item);
        updateUI();

        return super.onOptionsItemSelected(item);
    }

    public void updateUI() {
        if (AuthUtil.userIsSignedIn()) {
            mGreetingTextView.setText("Welcome back, " + AuthUtil.getName());
            mSignInButton.setVisibility(View.GONE);
        } else {
            mGreetingTextView.setText(getString(R.string.greeting_message_to_not_signed_in) + "\n"
                    + getString(R.string.sign_in_message));
            mSignInButton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onTextChanged(CharSequence cs, int start, int count, int after) {
        switch (mHandleStatusTextView.getText().toString()) {
            case (AuthActivity.NEEDS_HANDLE):
                startActivity(new Intent(this, StoreUserInfoActivity.class));
                break;
        }
        mHandleStatusTextView.removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence cs, int start, int count, int after) { }
    @Override
    public void afterTextChanged(Editable s) {}

}
