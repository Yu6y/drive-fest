package com.example.drivefest;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.drivefest.viewmodel.SignupActivityViewModel;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText email, password, name;
    private TextView txt;
    private Button loginbtn;
    private ImageView imageView;
    private SignupActivityViewModel mSignupVM;
    private Uri imageUri;

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
        name = findViewById(R.id.signname);
        imageView = findViewById(R.id.user_photo);
        mSignupVM = new ViewModelProvider(this).get(SignupActivityViewModel.class);
        imageUri = null;



    }

    public void signUpBtnClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        String mailStr = email.getText().toString().trim();
        String nameStr = name.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        mSignupVM.signUpUser(mailStr, passwordStr, nameStr, imageUri);
        mSignupVM.getToastText().observe(this, new Observer<HashMap<Integer, String>>() {
            @Override
            public void onChanged(HashMap<Integer, String> response) {
                switch(response.keySet().iterator().next()){
                    case 0:
                        Toast.makeText(SignupActivity.this, "Success login", Toast.LENGTH_SHORT).show();
                        mSignupVM.getUpdateData().observe(SignupActivity.this, new Observer<HashMap<String, String>>() {
                            @Override
                            public void onChanged(HashMap<String, String> stringStringHashMap) {
                                Log.e("name",stringStringHashMap.get("name") );
                                mSignupVM.updateUserProfile(stringStringHashMap.get("name"), stringStringHashMap.get("uri"));

                            }
                        });
                        startActivity(intent);
                        finish();
                    case 1:
                        Toast.makeText(SignupActivity.this, "Success login", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        email.setError("Bad email");
                        break;
                    case 3:
                        password.setError("At least 5 letters");
                        break;
                    case 4:
                        email.setError("");
                        password.setError("At least 5 letters");
                        break;
                    case 5:
                        name.setError("At least 1 character");
                        break;
                }
            }

        });

    }

    public void textLogInActivityClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void imageOnClick(View view){
        if(Build.VERSION.SDK_INT >= 22)
            checkPermission();
        else
            openGallery();
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_MEDIA_IMAGES)
        != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, Manifest.permission.READ_MEDIA_IMAGES)){
                Toast.makeText(SignupActivity.this, "Accept required permission", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(SignupActivity.this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        1);
            }
        }
        else
            openGallery();
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1 && data != null){
            Log.e("tuu", "uuu");
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
