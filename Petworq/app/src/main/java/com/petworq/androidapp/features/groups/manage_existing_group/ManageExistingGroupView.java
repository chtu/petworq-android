package com.petworq.androidapp.features.groups.manage_existing_group;

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
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features.friends.pendingRequests.PendingRequestsAdapter;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.groups.Social2GroupsDu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class ManageExistingGroupView extends LinearLayout {

    private static final String TAG = "ManageExistingGroupView";

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private AppTool mAppTool;
    private CollectionReference mGroupsCollection;

    @BindView(R.id.current_groups_recyclerview)
    RecyclerView groupsList;

    public ManageExistingGroupView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ManageExistingGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ManageExistingGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        mGroupsCollection = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(AuthUtil.getUid())
                .collection(Social2GroupsDu.GROUPS_COLL);
        initList();
    }

    public void initList() {
        groupsList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        groupsList.setLayoutManager(mLayoutManager);

        // Get the current groups
        mGroupsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> arr = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String groupId = document.getId();
                        if (!groupId.equals(BaseDataUtil.EMPTY_COLL_DOC_NAME)) {
                            arr.add(groupId);
                        }
                    }
                    if (arr.size() > 0) {
                        String[] newArray = arr.toArray(new String[arr.size()]);
                        groupsList.setAdapter(new CurrentGroupsAdapter(newArray));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: " + task.getException());
                }
            }
        });

    }

    public void setAppTool(AppTool appTool) {
        this.mAppTool = appTool;
    }
}
