package com.example.drivefest.data.repository;

import android.media.metrics.Event;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.drivefest.data.model.EventShort;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseFirestoreRepository {
    private static final FirebaseFirestoreRepository instanceDB = new FirebaseFirestoreRepository();
    private static FirebaseFirestore mDatabase;

    private FirebaseFirestoreRepository(){
        mDatabase = FirebaseFirestore.getInstance();
    }
    public static FirebaseFirestoreRepository getDbInstance(){
        return instanceDB;
    }

    public HashMap<String, Object> getEventShort(){ // zwrocic dane
        HashMap<String, Object> response = new HashMap<>();
        mDatabase.collection("event_short")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<EventShort> eventList = new ArrayList<>();
                            EventShort event = new EventShort();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("success", document.getData().toString());
                                event.setData(document.getData());
                                eventList.add(event);
                            }
                        }
                        else{
                            Log.d("fail", task.getException().toString());
                            response.put("fail", task.getException().toString());
                        }
                    }
                });
        return response;
    }
}
