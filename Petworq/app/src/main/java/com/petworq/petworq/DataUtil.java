package com.petworq.petworq;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by charlietuttle on 4/7/18.
 * I made this class in an attempt to make querying the database easier, but it seems I have to create
 * the listeners manually when I need them.
 */

public class DataUtil {

    private static final String TAG = "DataUtil";
    public static final int HANDLE_LENGTH_MIN = 3;
    public static final int HANDLE_LENGTH_MAX = 20;

    private static TextView sTextView;

    public static void setTextViewFromDatabase(DatabaseReference databaseReference, TextView textView) {
        sTextView = textView;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
            public void onDataChange(DataSnapshot dataSnapShot) {
               sTextView.setText(dataSnapShot.getValue().toString());
           }
           @Override
            public void onCancelled(DatabaseError databaseError
           ) {
               Log.d(TAG, "getValue:" + databaseError.getDetails());
           }
        });
        sTextView = null;
    }

    public static boolean checkCharSeq(CharSequence charSeq) {
        for (int i = 0; i < charSeq.length(); i++) {
            if ( !characterIsAllowed(charSeq.charAt(i)) )
                return false;
        }
        return true;
    }

    public static boolean characterIsAllowed(char ch) {

        int asciiCode = (int) ch;

        if (asciiCode == 45
                || ( asciiCode >= 48 && asciiCode <= 57 )
                || ( asciiCode >= 65 && asciiCode <= 90 )
                || ( asciiCode >= 95 )
                || ( asciiCode >= 97 && asciiCode <= 122 )
                || ( asciiCode == 127 ) ) {
            return true;
        } else {
            return false;
        }
    }

    public static String validateHandle(String handle) {
        String returnMessage = "";
        if (handle.length() < HANDLE_LENGTH_MIN) {
            returnMessage = "Your handle needs to be at least " + HANDLE_LENGTH_MIN + " characters.";
        } else if (handle.length() == HANDLE_LENGTH_MAX) {
            returnMessage = "Your have reached the maximum number of characters.";
        } else {

        }
        return returnMessage;
    }
}


/*
// you can put this in the onStart() method
mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
    @Override
    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
        if (documentSnapshot.exists()) {
            String quoteText = documentSnapshot.getString(KEY);
            mView.setText(quoteText);
        } else if (e != null) {
            Log.w(TAG, "Got an exception!", e);
        }
    }
});

 */