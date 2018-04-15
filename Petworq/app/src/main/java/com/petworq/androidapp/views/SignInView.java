package com.petworq.androidapp.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petworq.androidapp.Authentication.AuthActivity;
import com.petworq.androidapp.AuthenticationComponent;
import com.petworq.androidapp.AuthenticationTool;
import com.petworq.androidapp.DaggerAuthenticationComponent;
import com.petworq.androidapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charlietuttle on 4/13/18.
 */

public class SignInView extends LinearLayout {

    private final static String TAG = "SignInView";

    private Context mContext;

    //@Inject ExampleInterface mExampleInterface;

    @BindView(R.id.sign_in_button)
    Button mButton;

    @BindView(R.id.greeting_textview)
    TextView mGreetingTextView;

    @OnClick(R.id.sign_in_button)
    public void signIn(View view) {
        Log.d(TAG, "Sign In button selected.");
        AuthenticationComponent authComponent = DaggerAuthenticationComponent.create();
        AuthenticationTool authTool = authComponent.getAuthenticationTool();
        authTool.signIn(mContext);
    }


    public SignInView (Context context, @Nullable AttributeSet attributes) {
        super(context, attributes);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

}
