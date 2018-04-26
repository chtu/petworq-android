package com.petworq.androidapp.features.messages;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by charlietuttle on 4/18/18.
 */

public class MessagesView extends LinearLayout {
    public MessagesView(Context context) {
        super(context);
    }

    public MessagesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MessagesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
