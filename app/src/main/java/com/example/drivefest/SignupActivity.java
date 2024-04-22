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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private EditText email, password;
    private TextView txt;
    private Button loginbtn;
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

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signmail);
        password = findViewById(R.id.signpass);
        loginbtn = findViewById(R.id.signbtn);
        txt = findViewById(R.id.logintxt);
    }

    public void signUpBtnClick(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if(checkMail(emailStr) && checkPass(passwordStr)){
            auth.createUserWithEmailAndPassword(emailStr, passwordStr)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(SignupActivity.this, "Success"
                            , Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, e.getMessage().toString()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });

        }else{
            if(!checkMail(emailStr))
                email.setError("Bad e-mail");
            if(!checkPass(passwordStr))
                email.setError("Bad password");
        }
    }

    public void textLogInActivityClick(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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