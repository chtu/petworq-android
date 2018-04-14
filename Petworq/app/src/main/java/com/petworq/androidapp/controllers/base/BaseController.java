package com.petworq.androidapp.controllers.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bluelinelabs.conductor.Controller;
import com.petworq.androidapp.R;

/**
 * Created by charlietuttle on 4/13/18.
 */

public abstract class BaseController extends Controller {

    protected BaseController(Bundle args) {
        super(args);
    }

    @Override
    public void onAttach(@NonNull View view) {
        super.onAttach(view);

        setTitle();
    }

    protected void setTitle() {
        String title = getTitle();
        AppCompatActivity mainActivity = (AppCompatActivity) getActivity();
        Toolbar toolbar = mainActivity.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    abstract public String getTitle();
}
