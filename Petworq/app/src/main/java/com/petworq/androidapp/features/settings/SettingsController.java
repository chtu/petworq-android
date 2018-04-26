package com.petworq.androidapp.features.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;
import com.petworq.androidapp.di.app.app_tool.AppTool;
import com.petworq.androidapp.features._base.BaseController;

/**
 * Created by charlietuttle on 4/18/18.
 */

public class SettingsController extends BaseController {

    public SettingsController(Bundle args) {
        super(args);
    }

    public SettingsController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        SettingsView settingsView = (SettingsView) inflater.inflate(R.layout.view_settings, container, false);
        return settingsView;
    }


    @Override
    public String getTitle() {
        return "Settings";
    }

}
