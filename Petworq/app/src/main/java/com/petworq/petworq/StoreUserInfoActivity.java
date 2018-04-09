package com.petworq.petworq;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StoreUserInfoActivity extends AppCompatActivity implements android.text.TextWatcher {

    private static final String TAG = "StoreUserInfoActivity";
    private static final String HANDLE_KEY = "handle";
    private static final String FULLNAME_KEY = "fullName";
    private static final String USERID_KEY = "userId";

    private static final String NAME_TAKEN = "&&&taken";
    private static final String NAME_AVAILABLE = "&&&available";

    private TextView mValidationTextView;
    private EditText mInsertHandleEditText;
    private Button mSubmitButton;
    private TextView mNameTakenStatus;
    private TextView mNewHandle;

    private DocumentReference mUsersDocRef;
    private DocumentReference mHandlesDocRef;

    private String mUserId;
    private boolean firstKeyPressed = false;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_handle);

        mValidationTextView = (TextView) findViewById(R.id.validation_textview);
        mInsertHandleEditText = (EditText) findViewById(R.id.handle_edittext);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mNameTakenStatus = (TextView) findViewById(R.id.store_user_info_from_db_name_status);
        mNewHandle = (TextView) findViewById(R.id.new_handle_textview);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mInsertHandleEditText.addTextChangedListener(this);
        mNameTakenStatus.addTextChangedListener(this);

        mUser = AuthUtil.getUser();
        mUserId = mUser.getUid();

        // Retrieve the users's document
        mUsersDocRef = FirebaseFirestore.getInstance().document("users/" + mUserId);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Submit button clicked.");

                String handle = mInsertHandleEditText.getText().toString();
                mNewHandle.setText(handle);

                mHandlesDocRef = FirebaseFirestore.getInstance().document("handles/" + handle.toLowerCase());

                // Check the database to see if the handle is already taken, and if it is, update the
                // invisible status textview to reflect the results.
                mHandlesDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            mNameTakenStatus.setText(NAME_TAKEN);
                        } else {
                            mNameTakenStatus.setText(NAME_AVAILABLE);
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroying activity");
    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int count, int after) {
        String newlyEnteredText = cs.toString();
        Log.d(TAG, "Newly entered text: " + newlyEnteredText);
        Log.d(TAG, "Newly Entered Text: " + newlyEnteredText + ", Code: " + NAME_TAKEN);

        if (newlyEnteredText.equals(NAME_AVAILABLE)) {
            Log.d(TAG, "Name is available");

            // If the name isn't taken
            String fullName = AuthUtil.getName();
            String handle = mNewHandle.getText().toString();

            // Create map for the "users" document
            Map<String, Object> dataToSave = new HashMap<String, Object>();
            dataToSave.put(HANDLE_KEY, handle);
            dataToSave.put(FULLNAME_KEY, fullName);

            // Create map for the "handles" document
            Map<String, Object> handleDocToSave = new HashMap<String, Object>();
            handleDocToSave.put(USERID_KEY, mUserId);

            // Set the data for the "handles" document
            mHandlesDocRef.set(handleDocToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Handles document has been updated.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Handles document didn't save.");
                }
            });

            // Set the data for the "users" document
            mUsersDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Document has been saved.");
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Document was not saved.", e);
                }
            });

            finish();
        } else if (newlyEnteredText.equals(NAME_TAKEN)) {
            Log.d(TAG, "Name is taken.");
            mValidationTextView.setText(getText(R.string.name_already_taken));
        } else {
            Log.d(TAG, "Validating name.");
            mValidationTextView.setText(DataUtil.validateHandle(cs.toString()));
        }
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
