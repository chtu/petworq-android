package com.petworq.androidapp.features.friends.add_new_friends;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charlietuttle on 4/25/18.
 */

public class AddFriendsView extends LinearLayout {

    private static String TAG = "AddFriendsView";

    @BindView(R.id.search_handle_edittext)
    EditText searchFriendsEditText;

    @BindView(R.id.search_results)
    LinearLayout searchResults;

    @BindView(R.id.user_found_textview)
    TextView userFoundTextView;

    @BindView(R.id.send_friend_request_button)
    Button sendFriendRequestButton;

    @BindView(R.id.no_users_found_layout)
    LinearLayout noUsersFoundResults;

    @BindView(R.id.no_users_found_textview)
    TextView noUsersFoundTextView;

    @BindView(R.id.your_handle_textview)
    TextView yourHandleTextView;



    public AddFriendsView(Context context) {
        super(context);
    }

    public AddFriendsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AddFriendsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    // TODO: Clean this up. Doesn't check if there's already a pending request. Possibly put this in an activity.
    @OnClick(R.id.search_friends_button)
    public void findUserHandle() {
        String handleToFind = searchFriendsEditText.getText().toString().toLowerCase();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(DataUtil.HANDLES_COLL).document(handleToFind);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds.exists()) {
                        String userId = (String) ds.get(DataUtil.HANDLES_FIELD_USER_ID);
                        String handle = (String) ds.get(DataUtil.HANDLES_FIELD_NORMAL_CASE_HANDLE);

                        String myUserId = AuthUtil.getUid();

                        if (userId.equals(myUserId)) {
                            noUsersFoundTextView.setVisibility(View.GONE);
                            yourHandleTextView.setVisibility(View.VISIBLE);

                            searchResults.setVisibility(View.GONE);
                            noUsersFoundResults.setVisibility(View.VISIBLE);
                        } else {
                            userFoundTextView.setText(userId);

                            searchResults.setVisibility(View.VISIBLE);
                            noUsersFoundResults.setVisibility(View.GONE);
                        }
                    } else {
                        // No users were found
                        noUsersFoundTextView.setVisibility(View.VISIBLE);
                        yourHandleTextView.setVisibility(View.GONE);

                        searchResults.setVisibility(View.GONE);
                        noUsersFoundResults.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "Error while retrieving user data.");
                }
            }
        });
    }

    @OnClick(R.id.send_friend_request_button)
    public void sendRequest() {
        String friendToRequest = (String) userFoundTextView.getText();
        String myUserId = AuthUtil.getUid();
        long currentTime = System.nanoTime();

        DataUtil.addPendingRequestFromSendingUserToReceivingUser(myUserId, friendToRequest, currentTime);
    }
}
