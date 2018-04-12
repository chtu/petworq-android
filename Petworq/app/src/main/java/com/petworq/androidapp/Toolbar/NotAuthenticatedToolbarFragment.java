package com.petworq.androidapp.Toolbar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;

public class NotAuthenticatedToolbarFragment extends Fragment {

    private Toolbar mToolbar;

    public NotAuthenticatedToolbarFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_not_auth_toolbar, container, false);
        mToolbar = (Toolbar) rootView.findViewById(R.id.petworq_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return rootView;
    }

}
