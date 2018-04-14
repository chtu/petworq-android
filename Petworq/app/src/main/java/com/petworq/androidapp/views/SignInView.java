package com.petworq.androidapp.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petworq.androidapp.PetworqApplication;
import com.petworq.androidapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charlietuttle on 4/13/18.
 */

public class SignInView extends LinearLayout {

    private final static String TAG = "SignInView";

    //@Inject ExampleInterface mExampleInterface;

    @BindView(R.id.sign_in_button)
    Button mButton;

    @BindView(R.id.greeting_textview)
    TextView mGreetingTextView;

    @OnClick(R.id.sign_in_button)
    public void signIn(View view) {
        Log.d(TAG, "Clicked the button.");
        mGreetingTextView.setText("The text is changed.");
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
