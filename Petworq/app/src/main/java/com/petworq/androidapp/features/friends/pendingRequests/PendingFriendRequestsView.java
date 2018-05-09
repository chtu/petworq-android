package com.petworq.androidapp.features.friends.pendingRequests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.petworq.androidapp.R;
import com.petworq.androidapp.features.friends.FriendsAdapter;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.pendingRequests.Social2PendingReqDu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by charlietuttle on 4/25/18.
 */

public class PendingFriendRequestsView extends LinearLayout {

    private static final String TAG = "Pending...View";

    private LinearLayoutManager mLayoutManager;
    private PendingRequestsAdapter mAdapter;
    private Context mContext;
    private CollectionReference mPendingRequestsCollection;

    @BindView(R.id.pending_friends_recyclerview)
    RecyclerView pendingFriendsList;

    public PendingFriendRequestsView(Context context) {
        super(context);
        mContext = context;
    }

    public PendingFriendRequestsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PendingFriendRequestsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        mPendingRequestsCollection = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(AuthUtil.getUid()).collection(Social2PendingReqDu.PENDING_REQ_COLL);

        pendingFriendsList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        pendingFriendsList.setLayoutManager(mLayoutManager);
        setRequestsList();
    }


    private void setRequestsList() {
        mPendingRequestsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> arr = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getId();
                        if (!userId.equals(BaseDataUtil.EMPTY_COLL_DOC_NAME)) {
                            String userHandle = (String) document.get(SocialDu.SOCIAL_FIELD_HANDLE);
                            Log.d(TAG, "UserId: " + userId);

                            arr.add(userId);

                        }
                    }
                    if (arr.size() > 0) {
                        String[] newArray = arr.toArray(new String[arr.size()]);
                        pendingFriendsList.setAdapter(new PendingRequestsAdapter(newArray));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: " + task.getException());
                }
            }
        });

    }
}
