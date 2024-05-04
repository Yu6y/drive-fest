package com.example.drivefest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.drivefest.viewmodel.SignupActivityViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView txt;
    private Button loginbtn;
    private SignupActivityViewModel mSignupVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.signmail);
        password = findViewById(R.id.signpass);
        loginbtn = findViewById(R.id.signbtn);
        txt = findViewById(R.id.logintxt);
        mSignupVM = new ViewModelProvider(this).get(SignupActivityViewModel.class);
    }

    public void signUpBtnClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        String mailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        mSignupVM.signUpUser(mailStr, passwordStr);
        mSignupVM.getToastText().observe(this, new Observer<HashMap<Integer, String>>() {
            @Override
            public void onChanged(HashMap<Integer, String> response) {
                switch(response.keySet().iterator().next()){
                    case 0:
                        Toast.makeText(SignupActivity.this, response.get(1).toString(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    case 1:
                        Toast.makeText(SignupActivity.this, response.get(2).toString(), Toast.LENGTH_SHORT).show();
                    case 2:
                        email.setError("");
                        break;
                    case 3:
                        password.setError("At least 5 letters");
                        break;
                    case 4:
                        email.setError("");
                        password.setError("At least 5 letters");
                        break;
                }
            }

        });
    }

    public void textLogInActivityClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
