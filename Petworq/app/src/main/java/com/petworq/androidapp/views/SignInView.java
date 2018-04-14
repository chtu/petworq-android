package com.petworq.androidapp.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.petworq.androidapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charlietuttle on 4/13/18.
 */

public class SignInView extends LinearLayout {

    private final static String TAG = "SignInView";

    @BindView(R.id.sign_in_button) Button mButton;

    @OnClick(R.id.sign_in_button)
    public void test(View view) {
        Log.d(TAG, "Clicked the button.");
    }


    public SignInView (Context context, @Nullable AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

}
