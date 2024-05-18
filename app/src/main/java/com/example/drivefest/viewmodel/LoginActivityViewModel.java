package com.example.drivefest.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.drivefest.data.repository.FirebaseAuthRepository;

import java.util.HashMap;

public class LoginActivityViewModel extends ViewModel{

    private FirebaseAuthRepository mAuth;
    private MutableLiveData<HashMap<Boolean, String>> toastText;

    public LoginActivityViewModel(){
        mAuth = FirebaseAuthRepository.getRepoInstance();
        toastText = new MutableLiveData<>();
        System.out.println("konstruktor");
    }
    public MutableLiveData<HashMap<Boolean, String>> getToastText(){
        //if(toastText == null)
            //toastText = new MutableLiveData<>();
        return toastText;
    }

    public void signInUser(String mail, String password) {
        if(checkCredentials(mail, password))
            toastText = mAuth.signInUser(mail, password);
        else
            toastText.setValue(new HashMap<Boolean, String>(){
                {
                    put(false, "Given credentials are incorrect.");
                }});
    }

    private boolean checkCredentials(String mail, String pass){
        boolean mailCheck, passCheck;
        //mailCheck =  Pattern.compile("^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
                //.matcher(mail)
                //.matches();
        if(mail == null || mail.isEmpty())
            mailCheck = false;
        else
            mailCheck = true;
        if(pass == null || pass.isEmpty())
            passCheck = false;
        else
            passCheck = true;

        return mailCheck && passCheck;
    }
}
