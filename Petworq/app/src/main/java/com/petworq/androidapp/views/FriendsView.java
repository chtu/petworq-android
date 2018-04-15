package com.petworq.androidapp.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.R;
import com.petworq.androidapp.UtilityClasses.AuthUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
