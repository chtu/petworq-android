package com.petworq.androidapp.features.groups.manage_existing_group;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.petworq.androidapp.R;
import com.petworq.androidapp.features.friends.FriendsAdapter;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.members.Groups2MembersDu;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.friends.Social2FriendsDu;
import com.petworq.androidapp.utilities.data_utilities.social.groupInvitations.Social2GroupInvDu;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteFriendsToGroupActivity extends AppCompatActivity {

    private static final String TAG = "InviteFriends";

    private LinearLayoutManager mLayoutManager;
    private FriendsToInviteToGroupAdapter mAdapter;
    private String mGroupId;
    private CollectionReference mFriendsCollection;

    @BindView(R.id.current_friends_recyclerview)
    RecyclerView friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends_to_group);
        ButterKnife.bind(this);
        getData(savedInstanceState);

        mFriendsCollection = FirebaseFirestore.getInstance().collection(SocialDu.SOCIAL_COLL)
                .document(AuthUtil.getUid())
                .collection(Social2FriendsDu.FRIENDS_COLL);
        mLayoutManager = new LinearLayoutManager(this);
        friendsList.setLayoutManager(mLayoutManager);
        initFriendsList();
    }


    private void getData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
                mGroupId = null;
            else
                mGroupId = extras.getString(CurrentGroupsAdapter.GROUP_ID);
        } else {
            mGroupId = (String) savedInstanceState.getSerializable(CurrentGroupsAdapter.GROUP_ID);
        }
    }

    private void initFriendsList() {
        getFriendsList();
    }

    private void getFriendsList() {
        mFriendsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> friendsArrayList = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getId();
                        if (!userId.equals(BaseDataUtil.EMPTY_COLL_DOC_NAME)) { // We have all friends, now check if they are already members of groupId
                            Log.d(TAG, "Retrieving data: " + userId + " => " + document.getData());
                            friendsArrayList.add(userId);
                        }
                    }

                    checkIfMemberOfGroup(friendsArrayList);

                    //String[] newArray = friendsArrayList.toArray(new String[friendsArrayList.size()]);
                    //friendsList.setAdapter(new FriendsToInviteToGroupAdapter(newArray, mGroupId));

                } else {
                    Log.d(TAG, "Error getting documents: " + task.getException());
                }
            }
        });
    }

    private void checkIfMemberOfGroup(final ArrayList<String> friends) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(GroupsDu.GROUPS_COLL)
                .document(mGroupId)
                .collection(Groups2MembersDu.MEMBERS_COLL);
        collRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> newList = friends;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userId = doc.getId();
                        newList = removeIfMemberOfGroup(userId, newList);
                    }

                    String[] arr = newList.toArray(new String[newList.size()]);
                    friendsList.setAdapter(new FriendsToInviteToGroupAdapter(arr, mGroupId));
                }
            }
        });
    }

    private ArrayList<String> removeIfMemberOfGroup(String userId, ArrayList<String> arr) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals(userId)) {
                arr.remove(i);
            }
        }
        return arr;
    }
}
