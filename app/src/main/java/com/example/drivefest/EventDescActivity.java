package com.example.drivefest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EventDescActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title, description, city, date, followers, tags;
    private Button followBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_desc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.event_desc), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.eventDesc_image);
        title = findViewById(R.id.eventDesc_title);
        description = findViewById(R.id.eventDesc_text);
        city = findViewById(R.id.eventDesc_city);
        date = findViewById(R.id.eventDesc_date);
        followers = findViewById(R.id.eventDesc_followers);
        tags = findViewById(R.id.eventDesc_tags);
        followBtn = findViewById(R.id.eventDesc_btn_follow);
    }
}