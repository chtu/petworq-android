package com.petworq.androidapp._main.navbar;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import java.util.Stack;


public class NavigationBar extends Toolbar {

    public final static int SIGN_IN = 0;
    public final static int FRIENDS = 1;
    public final static int SETTINGS = 2;
    public final static int TASKS = 4;
    public final static int NOTIFICATIONS = 5;
    public final static int MESSAGES = 6;
    public final static int PENDING_FRIEND_REQUESTS = 7;
    public final static int ADD_FRIENDS = 8;
    public final static int CREATE_NEW_GROUPS = 9;
    public final static int MANAGE_EXISTING_GROUPS = 10;
    public final static int PENDING_GROUP_INVITATIONS = 11;

    public final static int DEFAULT_PAGE = TASKS;

    private Stack<Integer> mPagesVisited;

    // CONSTRUCTORS
    public NavigationBar (Context context) {
        super(context);
        initPagesStack();
    }

    public NavigationBar (Context context, AttributeSet attrs) {
        super(context, attrs);
        initPagesStack();
    }

    public NavigationBar (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPagesStack();
    }

    // Public methods
    public boolean isCurrentPage(int newPageCode) {
        int oldPageCode = mPagesVisited.peek();
        if (oldPageCode == newPageCode)
            return true;
        else
            return false;
    }

    public void pushToPagesVisited(int pageCode) {
        mPagesVisited.push(pageCode);
    }

    public int popPagesVisited() {
        return mPagesVisited.pop();
    }

    public void clearPagesVisited() {
        mPagesVisited.clear();
    }


    // Private Helper Methods

    private void initPagesStack() {
        mPagesVisited = new Stack<Integer>();
    }
}

