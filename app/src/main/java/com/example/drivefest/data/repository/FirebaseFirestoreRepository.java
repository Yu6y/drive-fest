package com.example.drivefest.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FirebaseFirestoreRepository {
    private static final FirebaseFirestoreRepository instanceDB = new FirebaseFirestoreRepository();
    private static FirebaseFirestore mDatabase;

    private FirebaseFirestoreRepository(){
        mDatabase = FirebaseFirestore.getInstance();
    }
    public static FirebaseFirestoreRepository getDbInstance(){
        return instanceDB;
    }

    public void getEventShort(){ // zwrocic dane
        mDatabase.collection("event_short")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("success", document.getData().toString());
                            }
                        }
                        else{
                            Log.d("fail", task.getException().toString());
                        }
                    }
                });
        //zwrocic dane
    }
}
