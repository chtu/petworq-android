package com.petworq.petworq.Authentication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.petworq.petworq.R;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final int RC_SIGN_IN = 123;

    private Button mSignInButton;

    public SignInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mSignInButton = rootView.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.sign_in_button):
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.startActivity(new Intent(activity, AuthActivity.class));
                break;
        }
    }
}
