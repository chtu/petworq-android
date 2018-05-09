package com.petworq.androidapp.features.groups.manage_existing_group;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.groupInvitations.Social2GroupInvDu;
import com.petworq.androidapp.utilities.data_utilities.social.groups.Social2GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.users.UsersDu;

/**
 * Created by charlietuttle on 4/30/18.
 */

public class FriendsToInviteToGroupAdapter extends RecyclerView.Adapter<FriendsToInviteToGroupAdapter.ViewHolder> {

    private String[] mDataSet;
    private String mGroupId;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout mLinearLayout;
        private TextView mHandleTextView;
        private Button mInviteFriendButton;
        private TextView mGroupIdHiddenView;

        private String mUserId;

        public ViewHolder(LinearLayout l) {
            super(l);
            mLinearLayout = l;

            mHandleTextView = (TextView) l.findViewById(R.id.handle_textview);
            mGroupIdHiddenView = (TextView) l.findViewById(R.id.group_id_hiddenview);
            mInviteFriendButton = (Button) l.findViewById(R.id.invite_friend_button);
            mInviteFriendButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case (R.id.invite_friend_button):
                    String currentUserId = AuthUtil.getUid();
                    String groupId = mGroupIdHiddenView.getText().toString();
                    long datetime = System.nanoTime();
                    Social2GroupInvDu.sendInvitation(currentUserId, mUserId, groupId, datetime);
                    setButtonVisibility(false);
                    setBackgroundColor("#ddf4d4");
                    break;
                default:
                    break;
            }
        }

        public void setHandleTextView(String handle) {
            mHandleTextView.setText(handle);
        }

        public void setBackgroundColor(String newColor) {
            mLinearLayout.setBackgroundColor(Color.parseColor(newColor));
        }

        public void setGroupId(String groupId) {
            mGroupIdHiddenView.setText(groupId);
        }

        public void setUserId(String userId) {
            mUserId = userId;
        }

        public void setButtonVisibility(boolean bool) {
            if (bool)
                mInviteFriendButton.setVisibility(View.VISIBLE);
            else
                mInviteFriendButton.setVisibility(View.GONE);
        }

        public String getUserId() { return this.mUserId; }
    }

    public FriendsToInviteToGroupAdapter(String[] dataset, String groupId) {
        this.mDataSet = dataset;
        this.mGroupId = groupId;
    }

    @Override
    public FriendsToInviteToGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout  l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_invite_friends_to_group_list_item, parent, false);
        ViewHolder vh = new ViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setGroupId(mGroupId);
        holder.setUserId(mDataSet[position]);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(UsersDu.USERS_COLL)
                .document(holder.getUserId());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                String displayName = ds.get(UsersDu.USERS_FIELD_DISPLAY_NAME).toString();
                String handle = ds.get(UsersDu.USERS_FIELD_HANDLE).toString();

                checkIfUserHasPendingRequest(holder, displayName, handle);
            }
        });
    }

    private void checkIfUserHasPendingRequest(
            final ViewHolder holder,
            final String displayName,
            final String handle) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(holder.getUserId())
                .collection(Social2GroupInvDu.GROUP_INV_COLL)
                .document(mGroupId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {

                String entry = displayName + ", @" + handle;

                if (ds.exists()) {
                    holder.setHandleTextView(entry + ", has already received an invitation for this group.");
                    holder.setButtonVisibility(false);
                    holder.setBackgroundColor("#bababa");
                } else {
                    holder.setHandleTextView(entry);
                    holder.setButtonVisibility(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
