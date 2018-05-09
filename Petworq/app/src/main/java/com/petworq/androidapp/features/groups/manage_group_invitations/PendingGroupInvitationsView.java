package com.petworq.androidapp.features.groups.manage_group_invitations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.petworq.androidapp.R;
import com.petworq.androidapp.features.friends.pendingRequests.PendingRequestsAdapter;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.groupInvitations.Social2GroupInvDu;
import com.petworq.androidapp.utilities.data_utilities.social.pendingRequests.Social2PendingReqDu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by charlietuttle on 4/30/18.
 */

public class PendingGroupInvitationsView extends LinearLayout {

    private static String TAG = "PendingGroups";

    private LinearLayoutManager mLayoutManager;
    private PendingRequestsAdapter mAdapter;
    private Context mContext;
    private CollectionReference mPendingGroupRequestsCollection;

    @BindView(R.id.pending_groups_recyclerview)
    RecyclerView pendingGroupsList;

    public PendingGroupInvitationsView(Context context) {
        super(context);
        mContext = context;
    }

    public PendingGroupInvitationsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PendingGroupInvitationsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        mPendingGroupRequestsCollection = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(AuthUtil.getUid()).collection(Social2GroupInvDu.GROUP_INV_COLL);

        pendingGroupsList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        pendingGroupsList.setLayoutManager(mLayoutManager);
        setRequestsList();
    }


    private void setRequestsList() {
        mPendingGroupRequestsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> arr = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String groupId = document.getId();
                        if (!groupId.equals(BaseDataUtil.EMPTY_COLL_DOC_NAME)) {
                            Log.d(TAG, "GroupId: " + groupId);

                            arr.add(groupId);
                        }
                    }
                    if (arr.size() > 0) {
                        String[] newArray = arr.toArray(new String[arr.size()]);
                        pendingGroupsList.setAdapter(new PendingGroupInvAdapter(newArray));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: " + task.getException());
                }
            }
        });

    }
}
