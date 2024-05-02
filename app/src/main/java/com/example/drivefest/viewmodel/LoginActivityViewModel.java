package com.example.drivefest.viewmodel;

import static com.example.drivefest.data.repository.FirebaseAuthRepository.getRepoInstance;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.LoginActivity;
import com.example.drivefest.data.repository.FirebaseAuthRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.errorprone.annotations.MustBeClosed;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.regex.Pattern;

public class LoginActivityViewModel extends ViewModel{

    private FirebaseAuthRepository mAuth;
    private MutableLiveData<HashMap<Boolean, String>> toastText;

    public LoginActivityViewModel(){
        mAuth = FirebaseAuthRepository.getRepoInstance();
    }
    public void signInUser(String email, String password) {
         toastText = mAuth.signInUser(email, password);
    }

    public MutableLiveData<HashMap<Boolean, String>> getToastText(){
        if(toastText == null)
            toastText = new MutableLiveData<>();
        return toastText;
    }

    private boolean checkCredentials(String mail, String pass){
        boolean mailCheck, passCheck;
        mailCheck =  Pattern.compile("^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
                .matcher(mail)
                .matches();
        if(pass == null || pass.isEmpty())
            passCheck = false;
        else
            passCheck = true;

        return mailCheck && passCheck;
    }
}
