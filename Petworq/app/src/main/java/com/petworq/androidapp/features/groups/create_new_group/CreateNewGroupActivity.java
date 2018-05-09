package com.petworq.androidapp.features.groups.create_new_group;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.petworq.androidapp.R;
import com.petworq.androidapp.utilities.AuthUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.pets.Groups2PetsDu;
import com.petworq.androidapp.utilities.data_utilities.groups.members.Groups2MembersDu;
import com.petworq.androidapp.utilities.data_utilities.groups.schedules.Groups2SchedulesDu;
import com.petworq.androidapp.utilities.data_utilities.groups.tasks.Groups2TasksDu;
import com.petworq.androidapp.utilities.data_utilities.social.SocialDu;
import com.petworq.androidapp.utilities.data_utilities.social.groups.Social2GroupsDu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateNewGroupActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "CreateNewGroupActivity";

    @BindView(R.id.group_name_edittext)
    EditText insertNameEditText;

    @BindView(R.id.submit_group_name_button)
    Button submitGroupNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        ButterKnife.bind(this);
        insertNameEditText.addTextChangedListener(this);
    }

    @OnClick(R.id.submit_group_name_button)
    public void createNewGroup() {
        Log.d(TAG, "Creating a new group.");
        String groupName = insertNameEditText.getText().toString();
        String userId = AuthUtil.getUid();
        String groupId = GroupsDu.createUniqueGroupId();
        long datetime = System.nanoTime();

        GroupsDu.setGroupFields(groupName, groupId);
        Groups2MembersDu.addUserToGroup(groupId, userId, Groups2MembersDu.ADMIN_ROLE);
        Social2GroupsDu.addGroup(groupId, userId, datetime);
        Groups2PetsDu.initPetData(groupId);
        Groups2SchedulesDu.initSchedulesCollection(groupId);
        Groups2TasksDu.initTasksCollection(groupId);
        SocialDu.initDefaultGroupIfNotSet(userId, groupId);
        finish();
    }

    @OnClick(R.id.go_back_button)
    public void goBack() {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0 && !submitGroupNameButton.isShown()) {
            submitGroupNameButton.setVisibility(View.VISIBLE);
        } else if (charSequence.length() == 0 && submitGroupNameButton.isShown()) {
            submitGroupNameButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
