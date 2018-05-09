package com.petworq.androidapp.features.friends.pendingRequests;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.friends.Social2FriendsDu;
import com.petworq.androidapp.utilities.data_utilities.social.pendingRequests.Social2PendingReqDu;

/**
 * Created by charlietuttle on 4/25/18.
 */

public class PendingRequestsAdapter extends RecyclerView.Adapter<PendingRequestsAdapter.ViewHolder>{
    private static final String TAG = "PendingRequestsAdapter";

    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout mLinearLayout;
        private TextView mUserHandleTextView;
        private TextView mUserIdTextView;
        private Button mConfirmButton;
        private Button mDenyButton;

        public ViewHolder(LinearLayout l) {
            super(l);
            mLinearLayout = l;
            mUserHandleTextView = l.findViewById(R.id.user_handle_textview);
            mUserIdTextView = l.findViewById(R.id.user_id_hiddenview);
            mConfirmButton = l.findViewById(R.id.confirm_friend_button);
            mDenyButton = l.findViewById(R.id.deny_friend_button);

            mConfirmButton.setOnClickListener(this);
            mDenyButton.setOnClickListener(this);
        }
        public LinearLayout getView() { return mLinearLayout; }

        public void setUserHandle(String handle) {
            mUserHandleTextView.setText(handle);
        }

        public void setUserId(String userId) {
            mUserIdTextView.setText(userId);
        }

        // TODO: insert time dependency
        @Override
        public void onClick(View view) {
            String userId = AuthUtil.getUid();
            String requestUserId = (String) mUserIdTextView.getText();

            switch (view.getId()) {
                case (R.id.confirm_friend_button):
                    Log.d(TAG, "Confirm button pressed.");
                    Social2FriendsDu.confirmPendingRequestBetweenTwoUsers(userId, requestUserId, System.nanoTime());
                    mLinearLayout.setBackgroundColor(Color.parseColor("#aaff99"));
                    break;
                case (R.id.deny_friend_button):
                    Log.d(TAG, "Deny button pressed.");
                    mLinearLayout.setBackgroundColor(Color.parseColor("#ff8e68"));
                    break;
                default:
                    break;
            }
            Social2PendingReqDu.removePendingRequestFromSendingUserToReceivingUser(requestUserId, userId);
            removeButtons();
        }

        private void removeButtons() {
            mConfirmButton.setVisibility(View.GONE);
            mDenyButton.setVisibility(View.GONE);
        }
    }

    public PendingRequestsAdapter (String[] myDataset) {
        this.mDataset = myDataset;
    }

    @Override
    public PendingRequestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_friends_pending_requests_list_item, parent, false);
        ViewHolder vh = new ViewHolder(l);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String userId = mDataset[position];

        // set the user as displayed in the list
        holder.setUserId(userId);

        // Get the handle of the user.
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL).document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                String handle = (String) ds.get(SocialDu.SOCIAL_FIELD_HANDLE);
                holder.setUserHandle(handle);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting the handle from the database.");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
