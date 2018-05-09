package com.petworq.androidapp.features.tasks.schedules;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.petworq.androidapp.R;
import com.petworq.androidapp.features.groups.manage_existing_group.CurrentGroupsAdapter;
import com.petworq.androidapp.utilities.data_utilities.groups.schedules.Groups2SchedulesDu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateNewScheduleActivity extends AppCompatActivity {

    @BindView(R.id.time_picker)
    TimePicker timePicker;

    @BindView(R.id.action_edittext)
    EditText actionEditText;

    private String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_schedule);
        ButterKnife.bind(this);
        getData(savedInstanceState);
        timePicker.setIs24HourView(true);
    }

    @OnClick(R.id.submit_button)
    public void submitScheduleData() {
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        long time = System.nanoTime();
        String action = actionEditText.getText().toString();

        Groups2SchedulesDu.createSchedule(mGroupId, time, action, hour, minute);
        finish();
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
}
