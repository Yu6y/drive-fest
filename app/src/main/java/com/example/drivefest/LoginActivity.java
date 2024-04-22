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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;

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

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.loginmail);
        password = findViewById(R.id.loginpass);
        loginbtn = findViewById(R.id.loginbtn);
        txt = findViewById(R.id.signuptxt);
    }

    public void textSingUpActivityClick(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void logInBtnClick(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if(!checkCredentials(emailStr, passwordStr))
            Toast.makeText(this, "Provided e-mail or password are incorrect."
                    , Toast.LENGTH_SHORT).show();
        else{
            intent.putExtra("user", emailStr);
            auth.signInWithEmailAndPassword(emailStr, passwordStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "Success login.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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