package com.petworq.androidapp.Toolbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.FriendsFragment;
import com.petworq.androidapp.MessagesFragment;
import com.petworq.androidapp.R;
import com.petworq.androidapp.UtilityClasses.AuthUtil;


public class ToolbarFragment extends Fragment {

    private Toolbar mToolbar;
    private AppCompatActivity mActivity;
    private FragmentManager mFragmentManager;

    public static final int TOOLBAR_CONTAINER_ID = R.id.fragment_toolbar;
    public static final int CONTENT_CONTAINER_ID = R.id.fragment_container;

    public ToolbarFragment () {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) this.getActivity();
        mFragmentManager = mActivity.getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_toolbar, container, false);
        mToolbar = (Toolbar) rootView.findViewById(R.id.petworq_toolbar);
        mActivity.setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case(R.id.messages_menuitem):
                switchToMessagesFragment();
                break;
            case (R.id.sign_out_menuitem):
                AuthUtil.signOut(mActivity);
                break;
            case (R.id.friends_menuitem):
                switchToFriendsFragment();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchToMessagesFragment() {
        MessagesFragment newFragment = new MessagesFragment();
        switchContentFragmentAndAddToBackStack(newFragment);
    }

    private void switchToFriendsFragment() {
        FriendsFragment newFragment = new FriendsFragment();
        switchContentFragmentAndAddToBackStack(newFragment);
    }

    private void switchContentFragmentAndAddToBackStack(final Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(CONTENT_CONTAINER_ID, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
