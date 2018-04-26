package com.petworq.androidapp.features.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;

/**
 * Created by charlietuttle on 4/16/18.
 */


public class StoreUserInfoActivity extends AppCompatActivity implements android.text.TextWatcher {

    private static final String TAG = "StoreUserInfoActivity";

    private TextView mValidationTextView;
    private EditText mInsertHandleEditText;
    private Button mSubmitButton;

    private DocumentReference mHandlesDocRef;

    private String mUserId;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_handle);

        mValidationTextView = (TextView) findViewById(R.id.validation_textview);
        mInsertHandleEditText = (EditText) findViewById(R.id.handle_edittext);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mInsertHandleEditText.addTextChangedListener(this);

        mUser = AuthUtil.getUser();
        mUserId = mUser.getUid();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Submit button clicked.");

                final String handle = mInsertHandleEditText.getText().toString();

                if (handle.length() >= DataUtil.HANDLE_LENGTH_MIN) {

                    mHandlesDocRef = FirebaseFirestore.getInstance().document("handles/" + handle.toLowerCase());
                    mUser = AuthUtil.getUser();
                    mUserId = mUser.getUid();
                    final String fullName = mUser.getDisplayName();
                    final String email = mUser.getEmail();

                    // Check the database to see if the handle is already taken
                    mHandlesDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                mValidationTextView.setText(getString(R.string.name_already_taken));
                            } else {
                                DataUtil.initSocialCollection(mUserId);
                                DataUtil.addUserToDatabase(mUserId, fullName, handle, email);
                                DataUtil.addHandleToDatabase(handle, mUserId, fullName);
                                DataUtil.initializePendingRequests(mUserId);
                                DataUtil.initializeFriends(mUserId);
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    });

                } else {
                    mValidationTextView.setText("Your handle needs to be at least " + DataUtil.HANDLE_LENGTH_MIN + " characters.");
                }
            }
        });
    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int count, int after) {
        Log.d(TAG, "Validating name.");
        mValidationTextView.setText(DataUtil.validateHandle(cs.toString()));
    }

    @Override
    public void beforeTextChanged(CharSequence cs, int start, int count, int after) { }
    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}