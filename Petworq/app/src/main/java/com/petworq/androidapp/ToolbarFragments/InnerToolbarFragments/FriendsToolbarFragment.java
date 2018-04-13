package com.petworq.androidapp.ToolbarFragments.InnerToolbarFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsToolbarFragment extends Fragment {


    public FriendsToolbarFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inner_toolbar_friends, container, false);
    }

}
