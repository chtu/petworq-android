package com.petworq.androidapp.features.friends;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.DataUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charlietuttle on 4/15/18.
 */

public class FriendsView extends LinearLayout implements TextWatcher {

    private final static String TAG = "FriendsView";

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private FirebaseFirestore mDb;
    private CollectionReference mFriendsColl;
    private Context mContext;
    private ArrayList<String> mFriendsArrayList;

    @BindView(R.id.search_friends_edittext)
    EditText searchFriendsEditText;

    @BindView(R.id.friend_request_linearlayout)
    LinearLayout userInDatabaseLayout;

    @BindView(R.id.user_found_in_database_textview)
    TextView userInDatabaseTextView;

    @BindView(R.id.request_friend_button)
    Button sendRequestButton;

    @BindView(R.id.my_friends_list)
    RecyclerView friendsListRecyclerView;

    public FriendsView (Context context, @Nullable AttributeSet attributes) {
        super(context, attributes);
        this.mContext = context;
        mDb = FirebaseFirestore.getInstance();
        mFriendsColl = mDb.collection(DataUtil.SOCIAL_COLL).document(AuthUtil.getUid()).collection(DataUtil.FRIENDS_COLL);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        searchFriendsEditText.addTextChangedListener(this);
        initVariables(mContext);
    }


    private void initVariables(Context context) {
        friendsListRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        friendsListRecyclerView.setLayoutManager(mLayoutManager);

        mFriendsArrayList = new ArrayList<String>();
        initFriendsList(friendsListRecyclerView, mFriendsArrayList);
    }




    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // If nothing is entered, display all friends.
        if (charSequence.length() == 0) {
            Log.d(TAG, "Text changed: sequence length = 0");
            setFriendsDisplayedToDefault();

        // If something is entered into the search box, display the friends or search for a friend in the database
        // if the user isn't currently friends with them.
        } else {
            Log.d(TAG, "Text changed: charSeq = " + charSequence);
            ArrayList<String> tempArrayList = getListOfFriendsWithHandlesContaining(charSequence);

            // No friends found, so search the database for the handle.
            if (tempArrayList.size() == 0) {
                if (friendsListRecyclerView.getVisibility() == View.VISIBLE
                        && userInDatabaseLayout.getVisibility() == View.GONE) {
                    friendsListRecyclerView.setVisibility(View.GONE);
                    userInDatabaseLayout.setVisibility(View.VISIBLE);
                }


            } else {
                String[] newArray = tempArrayList.toArray(new String[tempArrayList.size()]);
                friendsListRecyclerView.swapAdapter(new FriendsAdapter(newArray), false);
                if (friendsListRecyclerView.getVisibility() == View.GONE
                        && userInDatabaseLayout.getVisibility() == View.VISIBLE) {
                    friendsListRecyclerView.setVisibility(View.VISIBLE);
                    userInDatabaseLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    // Private helper methods for onTextChanged()

    private void setFriendsDisplayedToDefault() {
        String[] newArray = mFriendsArrayList.toArray(new String[mFriendsArrayList.size()]);
        friendsListRecyclerView.setAdapter(new FriendsAdapter(newArray));
    }


    private ArrayList<String> getListOfFriendsWithHandlesContaining(CharSequence charSequence) {
        ArrayList<String> tempArrayList = new ArrayList<String>();
        String handle;
        for (int index = 0; index < mFriendsArrayList.size(); index++) {
            handle = mFriendsArrayList.get(index);
            if (handle.contains(charSequence)) {
                Log.d(TAG, "Found match: " + handle);
                tempArrayList.add(handle);
            }
        }
        return tempArrayList;
    }


    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void initFriendsList(final RecyclerView recyclerView, final ArrayList<String> friendsArrayList) {
        mFriendsColl.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String handle = document.getId();
                        if (!handle.equals(DataUtil.EMPTY_COLL_DOC_NAME)) {
                            Log.d(TAG, "Retrieving data: " + handle + " => " + document.getData());
                            friendsArrayList.add(handle);
                        }
                    }

                    String[] newArray = friendsArrayList.toArray(new String[friendsArrayList.size()]);
                    recyclerView.setAdapter(new FriendsAdapter(newArray));
                } else {
                    Log.d(TAG, "Error getting documents: " + task.getException());
                }
            }
        });
    }

    //TODO: add a listener to check for new friend adds
}
