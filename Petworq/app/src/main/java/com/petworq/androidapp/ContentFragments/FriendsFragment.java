package com.petworq.androidapp.ContentFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.ContentFragments.InnerContentFragments.FriendsInnerContentFragment;
import com.petworq.androidapp.R;
import com.petworq.androidapp.ToolbarFragments.InnerToolbarFragments.FriendsToolbarFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private AppCompatActivity mActivity;
    private FragmentManager mFragmentManager;

    public static final String FRIENDS_INNER_TOOLBAR = "friends_inner_toolbar";

    public static final String FRIENDS_INNER_CONTENT_FRIENDS_TO_SHOW = "friends_inner_friends_to_show";


    public FriendsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AppCompatActivity) this.getActivity();
        mFragmentManager = mActivity.getSupportFragmentManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_inner_toolbar_container_for_friends, new FriendsToolbarFragment(), FRIENDS_INNER_TOOLBAR);
        fragmentTransaction.add(R.id.fragment_inner_content_container_for_friends, new FriendsInnerContentFragment(), FRIENDS_INNER_CONTENT_FRIENDS_TO_SHOW);
        fragmentTransaction.commit();

        return rootView;
    }

}
