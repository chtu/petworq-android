package com.petworq.androidapp;

import com.firebase.ui.auth.ui.phone.PhoneActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.UtilityClasses.DataUtil;

/**
 * Created by charlietuttle on 4/14/18.
 */

public class CloudFirestoreDataHandler implements DataHandler {

    private FirebaseFirestore db;

    public CloudFirestoreDataHandler() {
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public String getUserHandle(String userId) {
        /*
        DocumentReference docRef = db.collection("users").document(userId);
        final String str;

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    str = documentSnapshot.getData().get(DataUtil.USERS_FIELD_HANDLE).toString();
                } else {
                    return null;
                }
            }
        });
        */

        return null;
    }
}
