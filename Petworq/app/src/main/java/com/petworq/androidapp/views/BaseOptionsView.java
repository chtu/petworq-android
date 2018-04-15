package com.petworq.androidapp.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petworq.androidapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class BaseOptionsView extends LinearLayout{

    private final static String TAG = "BaseOptionsView";

    @BindView(R.id.no_groups_joined_textview)
    TextView noGroupsJoinedTextView;

    @BindView(R.id.create_new_group_button)
    Button nreateNewGroupButton;

    @BindView(R.id.search_friends_button)
    Button searchFriendsButton;

    @OnClick(R.id.search_friends_button)
    public void searchForFriends(View view) {
        Log.d(TAG, "Beginning friends activity.");
    }


    public BaseOptionsView (Context context, @Nullable AttributeSet attributes) {
        super(context, attributes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();;
        ButterKnife.bind(this);
    }
}
