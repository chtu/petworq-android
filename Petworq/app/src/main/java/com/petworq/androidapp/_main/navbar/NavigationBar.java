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
    public final static int NOTIFICATIONS = 4;

    public final static int DEFAULT_PAGE = TASKS;

    private Stack<Integer> mBackStack;

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
    public boolean onPage(int newPageCode) {
        int oldPageCode = mBackStack.peek();
        if (oldPageCode == newPageCode)
            return true;
        else
            return false;
    }

    public void pushToBackStack(int pageCode) {
        mBackStack.push(pageCode);
    }

    public int popBackStack() {
        return mBackStack.pop();
    }

    public void clearBackStack() {
        mBackStack.clear();
    }


    // Private Helper Methods

    private void initPagesStack() {
        mBackStack = new Stack<Integer>();
    }
}

// TODO: change BackStack to PagesVisited. Change onPage to isCurrentPage
