package com.example.drivefest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;

public class SortActivity extends AppCompatActivity {

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sort);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.xdefault);

        if(getIntent().hasExtra("sort")){
            String sort = getIntent().getStringExtra("sort");
            int id = getResources().getIdentifier(sort, "id", getPackageName());
            radioGroup.check(id);
        }
    }

    public void acceptSortClick(View view){
        RadioButton button = findViewById(radioGroup.getCheckedRadioButtonId());
        Intent intent = new Intent();
        intent.putExtra("sort", (Serializable) getResources().getResourceEntryName(button.getId()));
        setResult(RESULT_OK, intent);
        finish();

    }
}