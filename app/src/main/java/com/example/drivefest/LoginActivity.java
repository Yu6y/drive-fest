package com.example.drivefest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.drivefest.viewmodel.LoginActivityViewModel;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    LoginActivityViewModel mLoginVM;
    private EditText email, password;
    private TextView txt;
    private Button loginbtn;
    private final String SHARED_PREFS = "shared_prefs";
    private final String EMAIL_KEY = "email_key";
    private final String PASSWORD_KEY = "password_key";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.loginmail);
        password = findViewById(R.id.loginpass);
        loginbtn = findViewById(R.id.loginbtn);
        txt = findViewById(R.id.signuptxt);
        mLoginVM = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String mailSharedPref, passwordSharedPref;
        mailSharedPref = sharedPreferences.getString(EMAIL_KEY, null);
        passwordSharedPref = sharedPreferences.getString(PASSWORD_KEY, null);
        Log.e("activity", mailSharedPref + "  " + password);
        if(mailSharedPref != null && passwordSharedPref != null){

            Log.d("HomeActivity", "Retrieved Email: " + mailSharedPref);
            Log.d("HomeActivity", "Retrieved Password: " + passwordSharedPref);
            mLoginVM.signInUser(mailSharedPref, passwordSharedPref);
            mLoginVM.getToastText().observe(this, new Observer<HashMap<Boolean, String>>() {
                @Override
                public void onChanged(HashMap<Boolean, String> response) {
                    Toast.makeText(LoginActivity.this, response.get(response.keySet().iterator().next()).toString(), Toast.LENGTH_SHORT).show();

                    if (response.keySet().iterator().next().toString() == "true"){
                        email.setText(mailSharedPref);
                        password.setText(passwordSharedPref);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    mLoginVM.getToastText().removeObserver(this);
                }

            });
        }
    }

    public void textSingUpActivityClick(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void logInBtnClick(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        Log.d("Przed", "test");


        mLoginVM.signInUser(emailStr, passwordStr);
        mLoginVM.getToastText().observe(this, new Observer<HashMap<Boolean, String>>() {
            @Override
            public void onChanged(HashMap<Boolean, String> response) {
                Toast.makeText(LoginActivity.this, response.get(response.keySet().iterator().next()).toString(), Toast.LENGTH_SHORT).show();
                if (response.keySet().iterator().next().toString() == "true"){
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(EMAIL_KEY, emailStr);
                    editor.putString(PASSWORD_KEY, passwordStr);

                    editor.commit();

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }

        });

    }
}