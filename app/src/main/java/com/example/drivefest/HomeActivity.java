package com.example.drivefest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivefest.adapter.EventListAdapter;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private HomeViewModel homeVM;
    private EventListAdapter eventListAdapter;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        homeVM = new ViewModelProvider(this).get(HomeViewModel.class);
        homeVM.fetchEventShortList();

        list = findViewById(R.id.event_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));


        eventListAdapter = new EventListAdapter(HomeActivity.this, new ArrayList<>());
        list.setAdapter(eventListAdapter);
        homeVM.getEventShortList().observe(this, new Observer<List<EventShort>>() {
            @Override
            public void onChanged(List<EventShort> eventShorts) {
                eventListAdapter.updateData(eventShorts);
            }
        });


    }
}