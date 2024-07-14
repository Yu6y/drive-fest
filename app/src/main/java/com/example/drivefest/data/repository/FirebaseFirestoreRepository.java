package com.example.drivefest.data.repository;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.drivefest.data.model.Comment;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.model.Expense;
import com.example.drivefest.data.model.RatedWorkshop;
import com.example.drivefest.data.model.Workshop;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        mDatabase.collection("events_short")
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
        mDatabase.collection("events_short")
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
        mDatabase.collection("events_fav")
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
        mDatabase.collection("workshops_rated")
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
        mDatabase.collection("events_short")
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
        mDatabase.collection("events_short")
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

    public void postFavEvent(Map<String, Object> map, DatabaseDataCallback callback){
        mDatabase.collection("events_fav")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        List<String> response = new ArrayList<>();
                        response.add("Success");
                        callback.OnSuccess(response);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnFail(e.getMessage());
                    }
                });
    }
    public void updateFollowedEvent(String id, int count, DatabaseDataCallback callback){
        mDatabase.collection("events_short")
                .document(id)
                .update("followersCount", count)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        List<String> response = new ArrayList<>();
                        response.add("Success");
                        callback.OnSuccess(response);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnFail(e.getMessage());
                    }
                });
    }

    public void deleteFollowedEvent(String eventId, String userId, DatabaseDataCallback callback){
        mDatabase.collection("events_fav")
                .whereEqualTo("userId", userId)
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                doc.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        List<String> response = new ArrayList<>();
                                        response.add("success");
                                        callback.OnSuccess(response);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callback.OnFail(e.getMessage());
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public void addWorkshopRate(Map<String, Object> map, DatabaseDataCallback callback){
        mDatabase.collection("workshops_rated")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        List<String> response = new ArrayList<>();
                        response.add("Success");
                        callback.OnSuccess(response);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnFail(e.getMessage());
                    }
                });
    }

    public void updateRatedWorkshop(String id, Map<String, Long> ratings, DatabaseDataCallback callback){
        mDatabase.collection("workshops")
                .document(id)
                .update("ratings", ratings)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        List<String> response = new ArrayList<>();
                        response.add("Success");
                        callback.OnSuccess(response);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnFail(e.getMessage());
                    }
                });
    }

    public void postToDbEventWorkshop(Map<String, Object> map, String type, DatabaseDataCallback callback){
        String collection = "";
        if(type.equals("event"))
            collection = "events_short";
        else if(type.equals("workshop"))
            collection = "workshops";
        mDatabase.collection(collection)
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            if (documentReference != null) {
                                String documentId = documentReference.getId();
                                Log.d("DocumentSnapshot added with ID: ", documentId);
                                List<String> response = new ArrayList<>();
                                response.add(documentReference.getId());
                                callback.OnSuccess(response);
                            }
                        } else {
                            Log.w( "Error adding document", task.getException());
                            callback.OnFail(task.getException().getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnFail(e.getMessage());
                    }
                });
    }

    public void postToDbEventWorkshopDesc(Map<String, Object> map, String id, String type, DatabaseDataCallback callback){
        String collection = "", subcollection = "";
        if(type.equals("event")) {
            collection = "events_short";
            subcollection = "event_desc";
        }
        else if(type.equals("workshop")) {
            collection = "workshops";
            subcollection = "workshop_desc";
        }
        mDatabase.collection(collection)
                .document(id)
                .collection(subcollection)
                .document("0")
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        List<String> response = new ArrayList<>();
                        response.add("Success");
                        callback.OnSuccess(response);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnFail(e.getMessage());
                    }
                });
    }

    public void updateUser(String id, Map<String, Object> map){
        mDatabase.collection("users")
                .document(id)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("FirebaseAuth", "Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FirebaseAuth", "Fail");
                    }
                });
    }

    public void getCarRegister(String id, DatabaseDataCallback callback){
        mDatabase
                .collection("users")
                .document(id)
                .collection("register")
                .document("0")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> doc = document.getData();
                                Log.d("data", doc.toString());
                                List<Map<?, ?>> response = new ArrayList<>();
                                response.add(doc);
                                callback.OnSuccess(response);
                            } else {
                                Log.d("data", "Document does not exist");
                                callback.OnSuccess(null);
                            }
                        } else {
                            Log.e("CarRegister", "Error getting documents: ", task.getException());
                            callback.OnFail("Wystąpił błąd podczas odczytu danych!");
                        }
                    }
                });
    }

    public void updateRegisterTechData(Map<String, Object> map, String id, DatabaseDataCallback callback){
        mDatabase
                .collection("users")
                .document(id)
                .collection("register")
                .document("0")
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Dokument został pomyślnie zaktualizowany!");
                        List<String> response = new ArrayList<>();
                        response.add("success");
                        callback.OnSuccess(response);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Wystąpił błąd podczas aktualizacji dokumentu: ", e);
                        callback.OnFail("fail");
                    }
                });

    }

    public void deleteRegister(String id){
        mDatabase
                .collection("users")
                .document(id)
                .collection("register")
                .document("0")
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Firestore", "Success delete");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Fail");
                    }
                });
    }

    public void addExpense(Map<String, Object> map, String id, DatabaseDataCallback callback) {
        mDatabase
                .collection("users")
                .document(id)
                .collection("expenses")
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            if (documentReference != null) {
                                String documentId = documentReference.getId();
                                Log.d("DocumentSnapshot added with ID: ", documentId);
                                List<String> response = new ArrayList<>();
                                response.add(documentReference.getId());
                                callback.OnSuccess(response);
                            }
                        } else {
                            Log.w("Error adding document", task.getException());
                            callback.OnFail(task.getException().getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Wystąpił błąd podczas aktualizacji dokumentu: ", e);
                        callback.OnFail("fail");
                    }
                });
    }

    public void getExpensesList(String id, DatabaseDataCallback callback){
        mDatabase
                .collection("users")
                .document(id)
                .collection("expenses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Expense> expenseList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Expense expense = new Expense();
                                expense.setData(document.getData(), document.getId());
                                expenseList.add(expense);
                                Log.d("success", document.getData().toString());

                            }
                            callback.OnSuccess(expenseList);
                        } else {
                            Log.d("fail", task.getException().toString());
                            callback.OnFail(task.getException().getMessage());
                        }
                    }
                });
    }

    public void deleteExpenses(String id, DatabaseDataCallback callback){
        CollectionReference collectionRef =  mDatabase
                .collection("users")
                .document(id)
                .collection("expenses");

        collectionRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    collectionRef.document(document.getId()).delete()
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("Firestore", "Success");
                                            })
                                            .addOnFailureListener(e -> {
                                                callback.OnFail("Fail");
                                            });
                                }
                                List<String> response = new ArrayList<>();
                                response.add("Dane zostały usunięte!");
                                callback.OnSuccess(response);
                            } else {
                                callback.OnFail("Collection is Empty!");
                            }
                        } else {
                            callback.OnFail("Download data failed");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "fail");
                        callback.OnFail(e.getMessage());
                    }
                });
    }

    public void updateExpense(String userId, Map<String, Object> map, String id, DatabaseDataCallback callback){
        mDatabase
                .collection("users")
                .document(userId)
                .collection("expenses")
                .document(id)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "Update success");
                        List<String> response = new ArrayList<>();
                        response.add("Dane zapisano pomyślnie!");
                        callback.OnSuccess(response);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Update failed");
                        callback.OnFail("Bład w zapisie danych!");
                    }
                });
    }

    public void deleteExpense(String id, String userId){
        mDatabase
                .collection("users")
                .document(userId)
                .collection("expenses")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Firestore", "Success delete");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Delete failed");
                    }
                });
    }
}