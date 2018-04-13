package com.petworq.androidapp.ToolbarFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.petworq.androidapp.ContentFragments.BaseOptionsFragment;
import com.petworq.androidapp.ContentFragments.FriendsFragment;
import com.petworq.androidapp.ContentFragments.MessagesFragment;
import com.petworq.androidapp.ContentFragments.SettingsFragment;
import com.petworq.androidapp.ContentFragments.TasksFragment;
import com.petworq.androidapp.MainActivity;
import com.petworq.androidapp.R;
import com.petworq.androidapp.UtilityClasses.AuthUtil;


public class ToolbarFragment extends Fragment {

    public final static String TAG = "ToolbarFragment";

    private Toolbar mToolbar;
    private AppCompatActivity mActivity;
    private FragmentManager mFragmentManager;

    public static String MESSAGES = "messages";
    public static String FRIENDS = "friends";
    public static String BASE_OPTIONS = "base options";
    public static String TASKS = "tasks";
    public static String SETTINGS = "settings";

    private String[] mUserTags = { MESSAGES, FRIENDS, TASKS, SETTINGS, BASE_OPTIONS };

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

        initContentContainerToTasksList();

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
        Fragment oldFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        switch (item.getItemId()) {
            case (R.id.tasks_icon):
                changeToTasksScreen(oldFragment);
                break;
            case (R.id.messages_menuitem):
                changeToMessagesScreen(oldFragment);
                break;
            case (R.id.friends_menuitem):
                changeToFriendsScreen(oldFragment);
                break;
            case (R.id.settings_menuitem):
                changeToSettingsScreen(oldFragment);
                break;
            case (R.id.sign_out_menuitem):
                AuthUtil.signOut(mActivity);
                clearBackStack();
                for (int i = 0; i < mUserTags.length; i++) {
                    Log.d(TAG, "Removing " + mUserTags[i]);
                    removeFragment(mUserTags[i]);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initContentContainerToBaseOptions() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new BaseOptionsFragment(), BASE_OPTIONS);
        fragmentTransaction.commit();
    }


    private void initContentContainerToTasksList() {
        mToolbar.setTitle(R.string.tasks_title);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new TasksFragment(), TASKS);
        fragmentTransaction.commit();
    }

    private void changeToTasksScreen(Fragment oldFragment) {
        boolean onSameScreen = false;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (oldFragment != null) {
            if (oldFragment instanceof TasksFragment)
                onSameScreen = true;
            else
                fragmentTransaction.detach(oldFragment);
        }

        if (!onSameScreen) {
            mToolbar.setTitle(R.string.tasks_title);
            TasksFragment tasksFragment = (TasksFragment) mFragmentManager.findFragmentByTag(TASKS);

            if (tasksFragment == null)
                fragmentTransaction.add(R.id.fragment_container, new TasksFragment(), TASKS);
            else
                fragmentTransaction.attach(tasksFragment);

            if (MainActivity.BACK_ALLOWED) {
                fragmentTransaction.addToBackStack(TASKS);
            }
        }
        fragmentTransaction.commit();
    }

    private void changeToBaseOptionsScreen(Fragment oldFragment) {
        boolean onSameScreen = false;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (oldFragment != null) {
            if (oldFragment instanceof BaseOptionsFragment)
                onSameScreen = true;
            else
                fragmentTransaction.detach(oldFragment);
        }

        if (!onSameScreen) {
            BaseOptionsFragment baseOptionsFragment = (BaseOptionsFragment) mFragmentManager.findFragmentByTag(BASE_OPTIONS);

            if (baseOptionsFragment == null)
                fragmentTransaction.add(R.id.fragment_container, new BaseOptionsFragment(), BASE_OPTIONS);
            else
                fragmentTransaction.attach(baseOptionsFragment);

            if (MainActivity.BACK_ALLOWED) {
                fragmentTransaction.addToBackStack(BASE_OPTIONS);
            }
        }
        fragmentTransaction.commit();
    }


    private void changeToMessagesScreen(Fragment oldFragment) {
        boolean onSameScreen = false;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (oldFragment != null) {
            if (oldFragment instanceof MessagesFragment)
                onSameScreen = true;
            else
                fragmentTransaction.detach(oldFragment);
        }

        if (!onSameScreen) {
            mToolbar.setTitle(R.string.messages_title);
            MessagesFragment messagesFragment = (MessagesFragment) mFragmentManager.findFragmentByTag(MESSAGES);

            if (messagesFragment == null)
                fragmentTransaction.add(R.id.fragment_container, new MessagesFragment(), MESSAGES);
            else
                fragmentTransaction.attach(messagesFragment);

            if (MainActivity.BACK_ALLOWED) {
                fragmentTransaction.addToBackStack(MESSAGES);
            }
        }
        fragmentTransaction.commit();
    }

    private void changeToFriendsScreen(Fragment oldFragment) {
        boolean onSameScreen = false;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (oldFragment != null) {
            if ( oldFragment instanceof FriendsFragment )
                onSameScreen = true;
            else
                fragmentTransaction.detach(oldFragment);
        }

        if (!onSameScreen) {
            mToolbar.setTitle(R.string.friends_title);
            FriendsFragment friendsFragment = (FriendsFragment) mFragmentManager.findFragmentByTag(FRIENDS);

            if (friendsFragment == null)
                fragmentTransaction.add(R.id.fragment_container, new FriendsFragment(), FRIENDS);
            else
                fragmentTransaction.attach(friendsFragment);

            if (MainActivity.BACK_ALLOWED) {
                fragmentTransaction.addToBackStack(FRIENDS);
            }
        }
        fragmentTransaction.commit();
    }

    private void changeToSettingsScreen(Fragment oldFragment) {
        boolean onSameScreen = false;

        Log.d(TAG, "Creating transaction");
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (oldFragment != null) {
            if ( oldFragment instanceof SettingsFragment) {
                Log.d(TAG, "Same screen");
                onSameScreen = true;
            } else {
                fragmentTransaction.detach(oldFragment);
                Log.d(TAG, "detaching fragment");
            }
        }

        if (!onSameScreen) {
            Log.d(TAG, "Fuck this");
            mToolbar.setTitle(R.string.settings_title);
            SettingsFragment settingsFragment = (SettingsFragment) mFragmentManager.findFragmentByTag(SETTINGS);

            if (settingsFragment == null) {
                Log.d(TAG, "Null");
                fragmentTransaction.add(R.id.fragment_container, new SettingsFragment(), SETTINGS);
            } else {
                Log.d(TAG, "Not null");
                fragmentTransaction.attach(settingsFragment);
            }

            if (MainActivity.BACK_ALLOWED) {
                fragmentTransaction.addToBackStack(SETTINGS);
            }
        }
        fragmentTransaction.commit();
    }


    private void removeFragment(String tag) {
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    private void detachFragment(String tag) {
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.detach(fragment);
            fragmentTransaction.commit();
        }
    }


    private void clearBackStack() {
        if (MainActivity.BACK_ALLOWED) {
            Log.d(TAG, "Clearing backstack entry count of " + mFragmentManager.getBackStackEntryCount());
            for (int i = 0; i < mFragmentManager.getBackStackEntryCount() - 1; i++) {
                mFragmentManager.popBackStack();
            }
        }
    }
}
