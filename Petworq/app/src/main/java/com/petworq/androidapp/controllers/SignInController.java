package com.petworq.androidapp.controllers;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.petworq.androidapp.R;
import com.petworq.androidapp.views.SignInView;

public class SignInController extends Controller {

    private static final int RC_SIGN_IN = 123;

    public SignInController() {}


    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        SignInView signInView = (SignInView) inflater.inflate(R.layout.view_sign_in, container, false);
        return signInView;
    }

    /*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.sign_in_button):
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.startActivity(new Intent(activity, AuthActivity.class));
                break;
        }
    }

    */
}
