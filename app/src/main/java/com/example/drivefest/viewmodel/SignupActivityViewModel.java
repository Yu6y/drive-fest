package com.example.drivefest.viewmodel;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.repository.FirebaseAuthRepository;
import com.example.drivefest.data.repository.FirebaseFirestoreRepository;
import com.example.drivefest.data.repository.FirebaseStorageRepository;
import com.example.drivefest.data.repository.callback.DatabaseDataCallback;
import com.example.drivefest.data.repository.callback.StorageUrlCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivityViewModel extends ViewModel {
    private FirebaseAuthRepository mAuth;
    private FirebaseStorageRepository mStorage;
    private FirebaseFirestoreRepository mDb;
    private MutableLiveData<HashMap<Integer, String>> toastText;
    private MutableLiveData<HashMap<String, String>> updateData;

    public SignupActivityViewModel(){
        mAuth = FirebaseAuthRepository.getRepoInstance();
        mStorage = FirebaseStorageRepository.getStorageInstance();
        mDb = FirebaseFirestoreRepository.getDbInstance();
        toastText = new MutableLiveData<>();
        updateData = new MutableLiveData<>();
    }

    public MutableLiveData<HashMap<Integer, String>> getToastText() {
        return toastText;
    }
    public MutableLiveData<HashMap<String, String>> getUpdateData(){
        return updateData;
    }
    //zmiana
    public void signUpUser(String mail, String password, String name, Uri image) {
        if (checkMail(mail) && checkPass(password) && !name.isEmpty()) {
            mAuth.signUpUser(mail, password, new DatabaseDataCallback() {
                @Override
                public void OnSuccess(List<?> response) {
                    toastText.postValue((HashMap<Integer, String>) response.get(0));
                }

                @Override
                public void OnFail(String response) {
                    toastText.postValue(new HashMap<Integer, String>() {
                        {
                            put(0, "Success login.");
                        }
                    });
                }
            });
            if (image != null){
                mStorage.updatePhoto(image, new StorageUrlCallback() {
                    @Override
                    public void onUrlReceived(String url) {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("name", name);
                        Log.e("VM", name);
                        data.put("uri", url);
                        Log.e("debug svm", url);
                        updateData.postValue(data);
                        // Update user profile with the new URL
                        //updateUserProfile(name, url);
                    }
                });
        }else {
               // updateUserProfile(name, "https://firebasestorage.googleapis.com/v0/b/moto-event.appspot.com/o/images%2Fusers%2Fdefault.jpg?alt=media&token=98a3e130-f318-4581-9a71-21fa18755eba");
                HashMap<String, String> data = new HashMap<>();
                data.put("name", name);
                Log.e("VM", name);
                data.put("uri", "https://firebasestorage.googleapis.com/v0/b/moto-event.appspot.com/o/images%2Fusers%2Fdefault.jpg?alt=media&token=98a3e130-f318-4581-9a71-21fa18755eba");
                Log.e("debug svm", "https://firebasestorage.googleapis.com/v0/b/moto-event.appspot.com/o/images%2Fusers%2Fdefault.jpg?alt=media&token=98a3e130-f318-4581-9a71-21fa18755eba");
                updateData.postValue(data);
            }
        } else {
            if (!checkMail(mail)) {
                if (!checkPass(password)) {
                    toastText.setValue(new HashMap<Integer, String>() {
                        {
                            put(4, null);
                        }
                    });
                } else {
                    toastText.setValue(new HashMap<Integer, String>() {
                        {
                            put(2, null);
                        }
                    });
                }
            } else if (!checkPass(password)) {
                toastText.setValue(new HashMap<Integer, String>() {
                    {
                        put(3, null);
                    }
                });
            } else if (name.isEmpty()) {
                toastText.setValue(new HashMap<Integer, String>() {
                    {
                        put(5, null);
                    }
                });
            }
        }
    }

//zmiana
public void updateUserProfile(String name, String url) {
    UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(Uri.parse(url))
            .build();
    FirebaseUser user = mAuth.getCurrentUser();
    if (user != null) {
        Log.e("update profile" , updateProfile.getDisplayName());
        user.updateProfile(updateProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("complete", "Profile updated successfully.");
                } else {
                    Log.e("complete", "Profile update failed.");
                }
            }
        });
        Log.e("urlphoto", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "No photo URL");
    }
}


    private boolean checkMail(String mail){
        return Pattern.compile("^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
                .matcher(mail)
                .matches();
    }

    private boolean checkPass(String pass){
        if(pass == null || pass.isEmpty() || pass.length() < 6)
            return false;
        return true;
    }

    public void updateUserInDb(){
        Map<String, Object> map = new HashMap<>();
        map.put("null", "");
        mDb.updateUser(mAuth.getCurrentUserId(), map);
    }
}

