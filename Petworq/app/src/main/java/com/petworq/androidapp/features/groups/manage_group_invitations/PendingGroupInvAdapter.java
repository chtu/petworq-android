package com.petworq.androidapp.features.groups.manage_group_invitations;

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
import com.petworq.androidapp.features.friends.pendingRequests.PendingRequestsAdapter;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.members.Groups2MembersDu;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.friends.Social2FriendsDu;
import com.petworq.androidapp.utilities.data_utilities.social.groupInvitations.Social2GroupInvDu;
import com.petworq.androidapp.utilities.data_utilities.social.groups.Social2GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.social.pendingRequests.Social2PendingReqDu;

/**
 * Created by charlietuttle on 4/30/18.
 */

public class PendingGroupInvAdapter extends RecyclerView.Adapter<PendingGroupInvAdapter.ViewHolder>{
    private static final String TAG = "PendingGroupInvAdapter";

    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout mLinearLayout;
        private TextView mGroupIdTextView;
        private Button mConfirmButton;
        private Button mDenyButton;

        public ViewHolder(LinearLayout l) {
            super(l);
            mLinearLayout = l;
            mGroupIdTextView = l.findViewById(R.id.group_id_textview);
            mConfirmButton = l.findViewById(R.id.confirm_group_button);
            mDenyButton = l.findViewById(R.id.deny_group_button);

            mConfirmButton.setOnClickListener(this);
            mDenyButton.setOnClickListener(this);
        }
        public LinearLayout getView() { return mLinearLayout; }

        public void setGroupId(String groupId) {
            mGroupIdTextView.setText(groupId);
        }

        // TODO: insert time dependency
        @Override
        public void onClick(View view) {
            String userId = AuthUtil.getUid();
            String groupId = (String) mGroupIdTextView.getText();
            long datetime = System.nanoTime();

            switch (view.getId()) {
                case (R.id.confirm_group_button):
                    Log.d(TAG, "Confirm button pressed.");
                    Social2GroupsDu.addGroup(groupId, userId, datetime);
                    Groups2MembersDu.addUserToGroup(groupId, userId, Groups2MembersDu.ADMIN_ROLE);
                    mLinearLayout.setBackgroundColor(Color.parseColor("#aaff99"));
                    break;
                case (R.id.deny_group_button):
                    Log.d(TAG, "Deny button pressed.");
                    mLinearLayout.setBackgroundColor(Color.parseColor("#ff8e68"));
                    break;
                default:
                    break;
            }
            Social2GroupInvDu.deleteInvitation(userId, groupId);
            removeButtons();
        }

        private void removeButtons() {
            mConfirmButton.setVisibility(View.GONE);
            mDenyButton.setVisibility(View.GONE);
        }
    }

    public PendingGroupInvAdapter (String[] myDataset) {
        this.mDataset = myDataset;
    }

    @Override
    public PendingGroupInvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_groups_pending_invitations_list_item, parent, false);
        PendingGroupInvAdapter.ViewHolder vh = new PendingGroupInvAdapter.ViewHolder(l);
        return vh;
    }


    @Override
    public void onBindViewHolder(final PendingGroupInvAdapter.ViewHolder holder, int position) {
        String groupId = mDataset[position];

        // set the user as displayed in the list
        holder.setGroupId(groupId);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
