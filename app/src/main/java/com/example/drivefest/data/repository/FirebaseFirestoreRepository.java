package com.example.drivefest.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.drivefest.data.model.Event;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseFirestoreRepository {
    private static final FirebaseFirestoreRepository instanceDB = new FirebaseFirestoreRepository();
    private static FirebaseFirestore mDatabase;

    private FirebaseFirestoreRepository(){
        mDatabase = FirebaseFirestore.getInstance();
    }
    public static FirebaseFirestoreRepository getDbInstance(){
        return instanceDB;
    }

    public void getEventShort(DatabaseDataCallback callback){
            mDatabase.collection("event_short")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<EventShort> eventList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    EventShort event = new EventShort();
                                    event.setData(document.getData(), document.getId());
                                    eventList.add(event);
                                    Log.d("success", document.getData().toString());
                                    Log.d("count", String.valueOf(eventList.size()));

                                }
                                Log.d("coun2t", String.valueOf(eventList.size()));
                                callback.OnSuccess(eventList);
                            } else {
                                Log.d("fail", task.getException().toString());
                                callback.OnFail(task.getException().getMessage());
                            }

                        }
                    });
    }

    public void getEventDesc(String id, DatabaseDataCallback callback){
        mDatabase.collection("event_short")
                .document(id)
                .collection("event_desc")
                .document("0")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Map<String, Object> doc = task.getResult().getData();
                            Log.d("data", task.getResult().getData().toString());
                            List<Map<?, ?>> response = new ArrayList<>();
                            response.add(doc);
                            callback.OnSuccess(response);
                        }
                        else{
                            Log.e("EventDesc", "Error");
                            callback.OnFail("Fail");
                        }
                    }
                });
    }

}
