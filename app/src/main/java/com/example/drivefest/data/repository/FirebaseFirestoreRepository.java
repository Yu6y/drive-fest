package com.example.drivefest.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
                                    event.setData(document.getData());
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

}
