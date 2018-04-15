package com.petworq.androidapp.controllers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.petworq.androidapp.AppTool;
import com.petworq.androidapp.R;
import com.petworq.androidapp.controllers.base.BaseController;
import com.petworq.androidapp.views.SignInView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignInController extends BaseController {

    private static final int RC_SIGN_IN = 123;

    private Unbinder unbinder;

    public SignInController(Bundle args) {
        super(args);
    }

    public SignInController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }


    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        SignInView signInView = (SignInView) inflater.inflate(R.layout.view_sign_in, container, false);
        unbinder = ButterKnife.bind(this, signInView);
        return signInView;
    }


    @Override
    public void onDestroyView(View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }


    @Override
    public String getTitle() {
        return mAppTool.getContext().getString(R.string.sign_in);
    }

}
