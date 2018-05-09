package com.petworq.androidapp.utilities.data_utilities.groups.pets;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petworq.androidapp.utilities.data_utilities.BaseDataUtil;
import com.petworq.androidapp.utilities.data_utilities.groups.GroupsDu;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charlietuttle on 5/2/18.
 */

public class Groups2PetsDu extends BaseDataUtil {

    private static final String TAG = "Groups2PetsDu";
    private static final String COLL_NAME = "Groups-Pets";

    // Collections
    public static final String GROUPS_COLL = GroupsDu.GROUPS_COLL;
    public static final String PETS_COLL = "pets";

    // Fields
    public static final String NAME_FIELD = "name";
    public static final String SPECIES_FIELD = "species";
    public static final String DESC_FIELD = "description";



    public static void initPetData( String groupId ) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId)
                .collection(PETS_COLL);
        initCollection(collRef, COLL_NAME);
    }

    // Add the pet data to the database
    public static void setPetData(
            String groupId,
            String petId,
            String petName,
            String petSpecies,
            String petDescription ) {

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(GROUPS_COLL)
                .document(groupId)
                .collection(PETS_COLL)
                .document(petId);
        String logMsg = "Setting pet data for " + petName;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(NAME_FIELD, petName);
        map.put(SPECIES_FIELD, petSpecies);
        map.put(DESC_FIELD, petDescription);

        setDocRef(docRef, map, logMsg);
    }
}
