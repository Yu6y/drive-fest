package com.example.drivefest.data.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.drivefest.data.model.Image;
import com.example.drivefest.data.repository.callback.StorageUrlCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.net.StandardProtocolFamily;
import java.util.ArrayList;
import java.util.List;

public class FirebaseStorageRepository {
    private static final FirebaseStorageRepository instanceStorage = new FirebaseStorageRepository();

    private static FirebaseStorage storage;
    private StorageReference storageRef;
    Image image = new Image();

    private FirebaseStorageRepository(){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public static FirebaseStorageRepository getStorageInstance(){
        return instanceStorage;
    }

    public void getEventPhotoUrl(StorageUrlCallback callback, String name){
        storageRef = storageRef.getRoot();
        storageRef = storageRef.child("images/events/" + name);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callback.onUrlReceived(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onUrlReceived("");
            }
        });
    }
    public void getWorkshopPhotoUrl(StorageUrlCallback callback, String name){
        storageRef = storageRef.getRoot();
        storageRef = storageRef.child("images/workshops/" + name);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callback.onUrlReceived(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onUrlReceived("");
            }
        });
    }

}

