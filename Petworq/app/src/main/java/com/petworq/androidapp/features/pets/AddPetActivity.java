package com.petworq.androidapp.features.pets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.petworq.androidapp.R;
import com.petworq.androidapp.features.groups.manage_existing_group.CurrentGroupsAdapter;
import com.petworq.androidapp.utilities.data_utilities.groups.pets.Groups2PetsDu;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPetActivity extends AppCompatActivity {

    @BindView(R.id.pet_name_field)
    EditText petNameField;

    @BindView(R.id.pet_species_field)
    EditText petSpeciesField;

    @BindView(R.id.pet_description_field)
    EditText petDescriptionField;

    private String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        ButterKnife.bind(this);
        getData(savedInstanceState);
    }

    @OnClick(R.id.submit_button)
    public void setPetData(View view) {
        String petId = UUID.randomUUID().toString();
        String petName = petNameField.getText().toString();
        String petSpecies = petSpeciesField.getText().toString();
        String petDescription = petDescriptionField.getText().toString();

        Groups2PetsDu.setPetData(mGroupId, petId, petName, petSpecies, petDescription);
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
