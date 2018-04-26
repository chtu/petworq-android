package com.petworq.androidapp.features.friends.pendingRequests;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by charlietuttle on 4/25/18.
 */

public class PendingFriendRequestsView extends LinearLayout {

    public PendingFriendRequestsView(Context context) {
        super(context);
    }

    public PendingFriendRequestsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PendingFriendRequestsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
