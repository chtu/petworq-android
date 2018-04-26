package com.petworq.androidapp.features.messages;

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

public class MessagesController extends BaseController {


    public MessagesController(Bundle args) {
        super(args);
    }

    public MessagesController(AppTool appTool, Bundle args) {
        super(appTool, args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        MessagesView messagesView = (MessagesView) inflater.inflate(R.layout.view_messages, container, false);
        return messagesView;
    }


    @Override
    public String getTitle() {
        return "Messages";
    }

}
