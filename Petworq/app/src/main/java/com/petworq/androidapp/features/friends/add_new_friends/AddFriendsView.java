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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;
import com.petworq.androidapp.utilities.data_utilities.handles.HandlesDu;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.friends.Social2FriendsDu;
import com.petworq.androidapp.utilities.data_utilities.social.pendingRequests.Social2PendingReqDu;
import com.petworq.androidapp.utilities.data_utilities.users.UsersDu;

import java.util.logging.Handler;

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

    @BindView(R.id.already_has_pending_request)
    TextView alreadySentRequestTextView;

    @BindView(R.id.already_friend_textview)
    TextView alreadyFriendTextView;

    @BindView(R.id.user_id_textview)
    TextView userIdTextView;



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


    @OnClick(R.id.search_friends_button)
    public void findUserHandle() {
        String handleToFind = searchFriendsEditText.getText().toString().toLowerCase();

        if (handleToFind.length() > 0) {
            checkIfUserExists(handleToFind);
        } else {
            noUsersFoundTextView.setVisibility(View.GONE);
            alreadySentRequestTextView.setVisibility(View.GONE);
            yourHandleTextView.setVisibility(View.GONE);

            searchResults.setVisibility(View.GONE);
            noUsersFoundResults.setVisibility(View.GONE);
        }
    }

    private void checkIfUserExists(final String handle) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(HandlesDu.HANDLES_COLL)
                .document(handle);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {

                if (ds.exists()) {
                    // A user was found
                    String userId = ds.get(HandlesDu.HANDLES_FIELD_USER_ID).toString();

                    if (userId.equals(AuthUtil.getUid())) {     // If the user is me
                        noUsersFoundTextView.setVisibility(View.GONE);
                        alreadySentRequestTextView.setVisibility(View.GONE);
                        yourHandleTextView.setVisibility(View.VISIBLE);

                        searchResults.setVisibility(View.GONE);
                        noUsersFoundResults.setVisibility(View.VISIBLE);
                    } else {    // If the user is someone else
                        checkIfAlreadyFriends(handle, userId);
                    }

                } else {
                    // No users were found
                    noUsersFoundTextView.setVisibility(View.VISIBLE);
                    alreadyFriendTextView.setVisibility(View.GONE);
                    yourHandleTextView.setVisibility(View.GONE);
                    alreadySentRequestTextView.setVisibility(View.GONE);

                    searchResults.setVisibility(View.GONE);
                    noUsersFoundResults.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkIfAlreadyFriends(final String handle, final String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(AuthUtil.getUid())
                .collection(Social2FriendsDu.FRIENDS_COLL)
                .document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                if (ds.exists()) {          // This person is already a friend
                    noUsersFoundTextView.setVisibility(View.GONE);
                    alreadyFriendTextView.setVisibility(View.VISIBLE);
                    yourHandleTextView.setVisibility(View.GONE);
                    alreadySentRequestTextView.setVisibility(View.GONE);

                    searchResults.setVisibility(View.GONE);
                    noUsersFoundResults.setVisibility(View.VISIBLE);
                } else {        // This person is not yet a friend
                    checkPendingRequests(handle, userId);
                }
            }
        });
    }

    private void checkPendingRequests(final String handle, final String userId) {
        final String myUserId = AuthUtil.getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(userId)
                .collection(Social2PendingReqDu.PENDING_REQ_COLL)
                .document(myUserId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                if (ds.exists()) {      // User already received a pending request
                    noUsersFoundTextView.setVisibility(View.GONE);
                    alreadyFriendTextView.setVisibility(View.GONE);
                    alreadySentRequestTextView.setVisibility(View.VISIBLE);
                    yourHandleTextView.setVisibility(View.GONE);

                    searchResults.setVisibility(View.GONE);
                    noUsersFoundResults.setVisibility(View.VISIBLE);
                } else {        // User exists, is not yet a friend, and has not yet received a pending request
                    updateTextViews(handle, userId);
                }
            }
        });
    }

    private void updateTextViews(final String handle, final String userId) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(UsersDu.USERS_COLL)
                .document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                String displayName = ds.get(UsersDu.USERS_FIELD_DISPLAY_NAME).toString();
                String userFoundText = "User found: " + displayName + "\n@"
                        + handle;
                userFoundTextView.setText(userFoundText);
                userIdTextView.setText(userId);
                searchResults.setVisibility(View.VISIBLE);
                noUsersFoundResults.setVisibility(View.GONE);

            }
        });
    }


    @OnClick(R.id.send_friend_request_button)
    public void sendRequest() {
        String friendToRequest = (String) userIdTextView.getText();
        String myUserId = AuthUtil.getUid();
        long currentTime = System.nanoTime();

        Social2PendingReqDu.addPendingRequestFromSendingUserToReceivingUser(myUserId, friendToRequest, currentTime);
        noUsersFoundTextView.setVisibility(View.GONE);
        alreadyFriendTextView.setVisibility(View.GONE);
        alreadySentRequestTextView.setVisibility(View.GONE);
        yourHandleTextView.setVisibility(View.GONE);

        searchResults.setVisibility(View.GONE);
        noUsersFoundResults.setVisibility(View.GONE);
    }
}
