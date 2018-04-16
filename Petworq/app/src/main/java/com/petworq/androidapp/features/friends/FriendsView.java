package com.petworq.androidapp.features.friends;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petworq.androidapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by charlietuttle on 4/15/18.
 */

public class FriendsView extends LinearLayout {

    private final static String TAG = "FriendsView";

    private static String temp;

    @BindView(R.id.friends_greeting_textview)
    TextView friendsGreetingTextView;

    public FriendsView (Context context, @Nullable AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();;
        ButterKnife.bind(this);
    }
}
