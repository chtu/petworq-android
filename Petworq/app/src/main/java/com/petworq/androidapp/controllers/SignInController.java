package com.petworq.androidapp.controllers;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluelinelabs.conductor.Controller;
import com.petworq.androidapp.authentication.AuthActivity;
import com.petworq.androidapp.R;

public class SignInController extends Controller implements View.OnClickListener {

    private static final int RC_SIGN_IN = 123;

    private Button mSignInButton;

    public SignInController() {}


    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.view_sign_in, container, false);
        return view;
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
