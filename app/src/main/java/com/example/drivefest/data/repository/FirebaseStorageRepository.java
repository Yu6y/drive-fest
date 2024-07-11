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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.net.StandardProtocolFamily;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
//zmiana
public void updatePhoto(Uri photo, StorageUrlCallback callback) {
    // Get the reference to the desired directory
    //storageRef = storageRef.getRoot();
    StorageReference imagesRef = storageRef.child("images/users/");

    if (photo == null) {
        Log.e("storage", "null");
        StorageReference defaultImageRef = imagesRef.child("default.jpg");
        defaultImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("success", uri.toString());
                callback.onUrlReceived(uri.toString());
            }
        });
    } else {
        Log.e("storage", "notnull");
        StorageReference userImageRef = imagesRef.child(photo.getLastPathSegment());
        userImageRef.putFile(photo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("upload success", uri.toString());
                        callback.onUrlReceived(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("fail", e.getMessage());
                        callback.onUrlReceived("");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("upload fail", e.getMessage());
                callback.onUrlReceived("");
            }
        });
    }
}
//zmiana
/*public void addEventPhoto(Uri photo, StorageUrlCallback callback){
    storageRef = storageRef.getRoot();
    Log.e("storage", "notnull");

    String originalFileName = photo.getLastPathSegment();
    Log.e("original", originalFileName);
    String uniqueFileName = originalFileName + "_" + UUID.randomUUID().toString();
    Log.e("Generated FileName", uniqueFileName); // Log wygenerowanej nazwy pliku
    //StorageReference userImageRef = imagesRef.child(uniqueFileName);
    StorageReference imagesRef = storageRef.child("images/events/" + uniqueFileName);

    imagesRef.putFile(photo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.e("upload success", uri.toString()); // Log powodzenia przesyłania
                    callback.onUrlReceived(uniqueFileName); // Przekaż nazwę pliku do callbacka
                    Log.e("callback", "Unique file name sent to callback: " + uniqueFileName); // Log przekazanej nazwy pliku
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fail", e.getMessage());
                    callback.onUrlReceived(""); // Przekaż pusty string do callbacka w przypadku niepowodzenia
                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.e("upload fail", e.getMessage());
            callback.onUrlReceived("");
        }
    });
}*/
    public void addPhoto(Uri photo, String target, StorageUrlCallback callback){
        String fileName = photo.getLastPathSegment() + UUID.randomUUID().toString();

        storageRef = storageRef.getRoot();
        if(target.equals("event"))
            storageRef = storageRef.child("images/events/" + fileName);
        else if(target.equals("workshop"))
            storageRef = storageRef.child("images/workshops/" + fileName);
        storageRef.putFile(photo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("eventphoto", fileName);
                callback.onUrlReceived(fileName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("failphoto", e.getMessage());
                callback.onUrlReceived("");
            }
        });
    }
}

