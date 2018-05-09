package com.petworq.androidapp.features.groups.manage_existing_group;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.petworq.androidapp.R;
import com.petworq.androidapp.features.pets.AddPetActivity;
import com.petworq.androidapp.features.tasks.schedules.CreateNewScheduleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageGroupsActivity extends AppCompatActivity {

    @BindView(R.id.groupid_textview)
    TextView groupIdTextView;

    private String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);
        ButterKnife.bind(this);
        getData(savedInstanceState);
        updateGroupId();
    }

    private void updateGroupId() {
        groupIdTextView.setText(mGroupId);
    }

    private void getData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
                mGroupId = null;
            else
                mGroupId = extras.getString(CurrentGroupsAdapter.GROUP_ID);
        } else {
            mGroupId = (String) savedInstanceState.getSerializable(CurrentGroupsAdapter.GROUP_ID);
        }
    }

    @OnClick(R.id.invite_friend_button)
    public void startInviteFriendActivity() {
        Intent intent = new Intent(this, InviteFriendsToGroupActivity.class);
        intent.putExtra(CurrentGroupsAdapter.GROUP_ID, mGroupId);
        startActivity(intent);
    }

    @OnClick(R.id.add_pet_button)
    public void startAddPetActivity() {
        Intent intent = new Intent (this, AddPetActivity.class);
        intent.putExtra(CurrentGroupsAdapter.GROUP_ID, mGroupId);
        startActivity(intent);
    }

    @OnClick(R.id.add_new_schedule)
    public void startCreateScheduleActivity() {
        Intent intent = new Intent(this, CreateNewScheduleActivity.class);
        intent.putExtra(CurrentGroupsAdapter.GROUP_ID, mGroupId);
        startActivity(intent);
    }
}
