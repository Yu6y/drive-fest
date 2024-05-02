package com.example.drivefest.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseFirestoreRepository {
    private static final FirebaseFirestore instanceDB = FirebaseFirestore.getInstance();

    public static FirebaseFirestore getInstance(){
        return instanceDB;
    }
}
