package com.petworq.androidapp.features.groups.create_new_group;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.petworq.androidapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by charlietuttle on 4/29/18.
 */

public class CreateNewGroupView extends LinearLayout {

    private Context mContext;

    public CreateNewGroupView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CreateNewGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public CreateNewGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.create_new_group_button)
    public void startCreateGroupActivity() {
        Intent intent = new Intent(mContext, CreateNewGroupActivity.class);
        mContext.startActivity(intent);
    }
}
