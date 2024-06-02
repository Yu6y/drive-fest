package com.example.drivefest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.drivefest.data.model.Event;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.viewmodel.EventDescViewModel;
import com.example.drivefest.viewmodel.HomeViewModel;

public class EventDescActivity extends AppCompatActivity {

    private EventDescViewModel eventVM;
    private TextView title, text, city, date, tags, followers;
    private ImageView image;

    private Button btnFollow;
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
        eventVM = new ViewModelProvider(this).get(EventDescViewModel.class);
        eventVM.setEvent(getIntent().getParcelableExtra("event_id"));

        title = findViewById(R.id.eventDesc_title);
        text = findViewById(R.id.eventDesc_text);
        city = findViewById(R.id.eventDesc_city);
        date = findViewById(R.id.eventDesc_date);
        tags = findViewById(R.id.eventDesc_tags);
        followers = findViewById(R.id.eventDesc_followers);
        image = findViewById(R.id.eventDesc_image);
        btnFollow = findViewById(R.id.eventDesc_btn_follow);

        eventVM.getEvent().observe(this, new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                if (event != null) {
                    // Wykonanie metody updateEventDesc() po uzyskaniu eventId
                    eventVM.updateEventDesc();

                    title.setText(event.getName());
                    text.setText(event.getDescription());
                    city.setText(event.getLocation());
                    date.setText(event.getDate().toString());
                    String tag = "";
                    for(String s : event.getTags()){
                        tag += '#' + s + ", ";
                    }
                    tags.setText(tag);
                    followers.setText("Obserwujących: " + followers);
                }
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFollow.getText().equals("Obserwuj")) {
                    btnFollow.setText("Obserwujesz");
                    Drawable newIcon = ContextCompat.getDrawable(EventDescActivity.this, R.drawable.ic_favorite_white);
                    btnFollow.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                    btnFollow.setBackgroundResource(R.drawable.button_pressed);
                    btnFollow.setTextColor(ContextCompat.getColor(EventDescActivity.this, R.color.white));
                }
                else if(btnFollow.getText().equals("Obserwujesz")){
                    btnFollow.setText("Obserwuj");
                    Drawable newIcon = ContextCompat.getDrawable(EventDescActivity.this, R.drawable.ic_favorite);
                    btnFollow.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                    btnFollow.setBackgroundResource(R.drawable.custom_button);
                    btnFollow.setTextColor(ContextCompat.getColor(EventDescActivity.this, R.color.black));
                }
            }
        });
    }
}