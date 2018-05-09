package com.petworq.androidapp.features.groups.manage_existing_group;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.groups.Social2GroupsDu;

import org.w3c.dom.Document;

/**
 * Created by charlietuttle on 4/30/18.
 */

public class CurrentGroupsAdapter extends RecyclerView.Adapter<CurrentGroupsAdapter.ViewHolder> {

    public static final String GROUP_ID = "groupName";

    private String[] mDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "CurrentGroupsAdapter";

        private LinearLayout mLinearLayout;
        private TextView mGroupNameTextView;
        private TextView mGroupIdHiddenView;
        private Button mManageButtonTextView;
        private Button mMakeDefaultButton;

        public ViewHolder(LinearLayout l) {
            super(l);
            mLinearLayout = l;
            mGroupNameTextView = (TextView) l.findViewById(R.id.group_name_textview);
            mGroupIdHiddenView = (TextView) l.findViewById(R.id.group_id_textview);
            mManageButtonTextView = (Button) l.findViewById(R.id.manage_group_button);
            mMakeDefaultButton = (Button) l.findViewById(R.id.make_default_button);

            mManageButtonTextView.setOnClickListener(this);
            mMakeDefaultButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String groupId = mGroupIdHiddenView.getText().toString();
            switch (view.getId()) {
                case (R.id.manage_group_button):
                    Context context = mLinearLayout.getContext();
                    Intent intent = new Intent(context, ManageGroupsActivity.class);
                    intent.putExtra(GROUP_ID, groupId);
                    context.startActivity(intent);
                    break;
                case (R.id.make_default_button):
                    String userId = AuthUtil.getUid();
                    SocialDu.setDefaultGroup(userId, groupId);
                    hideMakeDefaultButton();
                    break;
                default:
                    break;
            }
        }

        public void setGroupName(String groupName) {
            mGroupNameTextView.setText(groupName);
        }

        public void setGroupId(String groupId) {
            mGroupIdHiddenView.setText(groupId);
        }

        public void showMakeDefaultButton() {
            mMakeDefaultButton.setVisibility(View.VISIBLE);
        }

        public void hideMakeDefaultButton() {
            mMakeDefaultButton.setVisibility(View.GONE);
        }

        public void setSnapshotChangedListener(String groupId) {

            DocumentReference docRef = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                    .document(AuthUtil.getUid());

            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot ds, FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (ds != null && ds.exists()) {
                        String groupId = mGroupIdHiddenView.getText().toString();
                        String defaultGroup = ds.get(SocialDu.SOCIAL_FIELD_DEFAULT_GROUP).toString();

                        if (groupId.equals(defaultGroup)) {
                            hideMakeDefaultButton();
                        } else {
                            showMakeDefaultButton();
                        }
                    }
                }
            });
        }
    }

    public CurrentGroupsAdapter (String[] dataset) {
        this.mDataset = dataset;
    }

    @Override
    public CurrentGroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_groups_manage_existing_list_item, parent, false);
        ViewHolder vh = new ViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String userId = AuthUtil.getUid();
        final String groupId = mDataset[position];
        holder.setGroupId(groupId);

        DocumentReference docRef1 = FirebaseFirestore.getInstance().collection(GroupsDu.GROUPS_COLL)
                .document(groupId);
        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                String groupName = ds.get(GroupsDu.GROUP_NAME_FIELD).toString();
                holder.setGroupName(groupName);
            }
        });

        DocumentReference docRef2 = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(userId);
        docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.get(SocialDu.SOCIAL_FIELD_DEFAULT_GROUP).toString()
                        .equals(groupId)) {
                    holder.showMakeDefaultButton();
                } else {
                    holder.hideMakeDefaultButton();
                }
            }
        });

        holder.setSnapshotChangedListener(groupId);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
