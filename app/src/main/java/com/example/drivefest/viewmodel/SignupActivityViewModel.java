package com.example.drivefest.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.repository.FirebaseAuthRepository;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignupActivityViewModel extends ViewModel {
    private FirebaseAuthRepository mAuth;
    private MutableLiveData<HashMap<Integer, String>> toastText;

    public SignupActivityViewModel(){
        mAuth = FirebaseAuthRepository.getRepoInstance();
        toastText = new MutableLiveData<>();
    }

    public MutableLiveData<HashMap<Integer, String>> getToastText() {
        return toastText;
    }
    public void signUpUser(String mail, String password) {
        if (checkMail(mail) && checkPass(password)) {
            toastText = mAuth.signUpUser(mail, password);
        }
        if (!checkMail(mail)) {
            if (!checkPass(password))
                toastText.setValue(new HashMap<Integer, String>() {
                    {
                        put(4, null);
                    }
                });
            else
                toastText.setValue(new HashMap<Integer, String>() {
                    {
                        put(2, null);
                    }
                });
        } else if (!checkPass(password)) {
            toastText.setValue(new HashMap<Integer, String>() {
                {
                    put(3, null);
                }
            });
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
}

