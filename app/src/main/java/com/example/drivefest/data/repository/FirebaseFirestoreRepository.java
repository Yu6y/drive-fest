package com.example.drivefest.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.drivefest.data.model.Comment;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.model.RatedWorkshop;
import com.example.drivefest.data.model.Workshop;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private FirebaseFirestoreRepository() {
        mDatabase = FirebaseFirestore.getInstance();
    }

    public static FirebaseFirestoreRepository getDbInstance() {
        return instanceDB;
    }

    public void getEventShort(DatabaseDataCallback callback) {
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

    public void getEventDesc(String id, DatabaseDataCallback callback) {
        mDatabase.collection("event_short")
                .document(id)
                .collection("event_desc")
                .document("0")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> doc = task.getResult().getData();
                            Log.d("data", task.getResult().getData().toString());
                            List<Map<?, ?>> response = new ArrayList<>();
                            response.add(doc);
                            callback.OnSuccess(response);
                        } else {
                            Log.e("EventDesc", "Error");
                            callback.OnFail("Fail");
                        }
                    }
                });
    }

    public void getFollowedEvents(String userId, DatabaseDataCallback callback) {//"hR3XI705oabbMqILbDTE3MpHBl43"
        mDatabase.collection("fav_events")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<EventShort> eventList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EventShort event = new EventShort();
                                event.setData(document.getData(), document.getData().get("eventId").toString());
                                eventList.add(event);
                                Log.d("successfavortie", document.getData().toString());
                                Log.d("countfav", String.valueOf(eventList.size()));

                            }
                            Log.d("coun2tfav", String.valueOf(eventList.size()));
                            callback.OnSuccess(eventList);
                        } else {
                            Log.d("failfav", task.getException().toString());
                            callback.OnFail(task.getException().getMessage());
                        }
                    }
                });
    }

    public void getWorkshops(DatabaseDataCallback callback) {
        mDatabase.collection("workshops")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Workshop> workshopsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Workshop workshop = new Workshop();
                                workshop.setData(document.getData(), document.getId());
                                workshopsList.add(workshop);
                                Log.d("success", document.getData().toString());
                                Log.d("count", String.valueOf(workshopsList.size()));

                            }
                            Log.d("coun2t", String.valueOf(workshopsList.size()));
                            callback.OnSuccess(workshopsList);
                        } else {
                            Log.d("fail", task.getException().toString());
                            callback.OnFail(task.getException().getMessage());
                        }

                    }
                });
    }

    public void getWorkshopDesc(String id, DatabaseDataCallback callback) {
        mDatabase.collection("workshops")
                .document(id)
                .collection("workshop_desc")
                .document("0")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> doc = task.getResult().getData();
                            Log.d("data", task.getResult().getData().toString());
                            List<Map<?, ?>> response = new ArrayList<>();
                            response.add(doc);
                            callback.OnSuccess(response);
                        } else {
                            Log.e("WorkshopDesc", "Error");
                            callback.OnFail("Fail");
                        }
                    }
                });
    }
   /* public void getWorkshopRate(String userId, DatabaseDataCallback callback) {
        mDatabase.collection("rated_workshops")
                .whereEqualTo("userId", "hR3XI705oabbMqILbDTE3MpHBl43")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<RatedWorkshop> ratedWorkshops = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RatedWorkshop ratedWorkshop = new RatedWorkshop();
                                ratedWorkshop.setData(document.getData());
                                ratedWorkshops.add(ratedWorkshop);
                                Log.d("successfavortie", document.getData().toString());
                                Log.d("countfav", String.valueOf(ratedWorkshops.size()));

                            }
                            Log.d("coun2tfav", String.valueOf(ratedWorkshops.size()));
                            callback.OnSuccess(ratedWorkshops);
                        } else {
                            Log.d("failfav", task.getException().toString());
                            callback.OnFail(task.getException().getMessage());
                        }
                    }
                });
    }*/

    public void getWorkshopRate(String userId, DatabaseDataCallback callback) {//"hR3XI705oabbMqILbDTE3MpHBl43"
        mDatabase.collection("rated_workshops")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<RatedWorkshop> ratedWorkshops = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RatedWorkshop ratedWorkshop = new RatedWorkshop();
                                ratedWorkshop.setData(document.getData());
                                ratedWorkshops.add(ratedWorkshop);
                                Log.d("successfavortie", document.getData().toString());
                                Log.d("countfav", String.valueOf(ratedWorkshops.size()));

                            }
                            Log.d("coun2tfav", String.valueOf(ratedWorkshops.size()));
                            callback.OnSuccess(ratedWorkshops);
                        } else {
                            Log.d("failfav", task.getException().toString());
                            callback.OnFail(task.getException().getMessage());
                        }
                    }
                });

    }

    public void postComment(String id, Map<String, Object> map, DatabaseDataCallback callback){
        mDatabase.collection("event_short")
                .document(id)
                .collection("event_desc")
                .document("0")
                .collection("comments")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        List<String> text = new ArrayList<>();
                        text.add("Comment added!");
                        callback.OnSuccess(text);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnFail(e.getMessage());
                    }
                });
    }

    public void fetchCommentsList(String id, DatabaseDataCallback callback) {
        mDatabase.collection("event_short")
                .document(id)
                .collection("event_desc")
                .document("0")
                .collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Comment> commentsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Comment comment = new Comment();
                                comment.setData(document.getData());
                                commentsList.add(comment);
                                Log.d("success", document.getData().toString());
                                Log.d("count", String.valueOf(commentsList.size()));

                            }
                            Log.d("coun2t", String.valueOf(commentsList.size()));
                            callback.OnSuccess(commentsList);
                        } else {
                            Log.d("fail", task.getException().toString());
                            callback.OnFail(task.getException().getMessage());
                        }

                    }
                });
    }
}