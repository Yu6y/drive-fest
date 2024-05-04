package com.example.drivefest;

import android.content.Intent;
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
        //mLoginVM = new LoginActivityViewModel();
        mLoginVM = new ViewModelProvider(this).get(LoginActivityViewModel.class);


    }

    public void textSingUpActivityClick(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void logInBtnClick(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        intent.putExtra("user", emailStr);

        Log.d("Przed", "test");


        mLoginVM.signInUser(emailStr, passwordStr);
        mLoginVM.getToastText().observe(this, new Observer<HashMap<Boolean, String>>() {
            @Override
            public void onChanged(HashMap<Boolean, String> response) {
                Toast.makeText(LoginActivity.this, response.get(response.keySet().iterator().next()).toString(), Toast.LENGTH_SHORT).show();
                if(response.keySet().iterator().next().toString() == "true")
                    startActivity(intent);
            }

        });

    }
}