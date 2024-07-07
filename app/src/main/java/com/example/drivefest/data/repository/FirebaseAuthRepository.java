package com.example.drivefest.data.repository;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.drivefest.LoginActivity;
import com.example.drivefest.SignupActivity;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseAuthRepository {
    private static final FirebaseAuthRepository instanceAuth = new FirebaseAuthRepository();
    private static FirebaseAuth mAuth;

    private FirebaseAuthRepository(){
        mAuth = FirebaseAuth.getInstance();
    }
    public static FirebaseAuthRepository getRepoInstance(){
        return instanceAuth;
    }
    public MutableLiveData<HashMap<Boolean, String>> signInUser(String mail, String password){
        final MutableLiveData<HashMap<Boolean, String>> response = new MutableLiveData<>();
        Log.d("Firebejs", "test");
        mAuth.signInWithEmailAndPassword(mail, password)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    response.setValue(new HashMap<Boolean, String>(){
                        {
                            put(true, "Success login.");
                        }});
            }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    response.setValue(new HashMap<Boolean, String>(){
                        {
                            put(false, e.getMessage());
                        }});
                }
            });
        return response;
    }

    public void signUpUser(String mail, String password, DatabaseDataCallback callback){
        List<HashMap<Integer, String>> response = new ArrayList<>();
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                    @Override
                    public void onSuccess(AuthResult authResult) {

                        response.add(new HashMap<Integer, String>(){
                            {
                                put(0, "Success login.");
                            }});
                        callback.OnSuccess(response);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.add(new HashMap<Integer, String>(){
                            {
                                put(1, "Success login.");
                            }});
                        callback.OnFail(e.toString());
                    }
                });
    }

    public String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return null;
        }
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public Uri getUser(){
        return mAuth.getCurrentUser().getPhotoUrl();
    }
}
