package com.example.drivefest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.drivefest.viewmodel.HomeViewModel;
import com.example.drivefest.adapter.EventClickListener;
import com.example.drivefest.adapter.EventListAdapter;
import com.example.drivefest.data.model.EventShort;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private HomeViewModel homeVM;
    private EventListAdapter eventListAdapter;
    private RecyclerView list;
    private DrawerLayout drawerLayout;
    private Button buttonFiltruj, buttonSortuj, buttonWyszukaj;
    private LinearLayout linearLayout;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.home_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.home_drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(toggle);
        //toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            toolbar.setPadding(0, topInset, 0, 0);
            return insets;
        });



        homeVM = new ViewModelProvider(this).get(HomeViewModel.class);
        homeVM.fetchEventShortList();

        list = findViewById(R.id.event_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));


        eventListAdapter = new EventListAdapter(HomeActivity.this, new ArrayList<>(), new EventClickListener() {
            @Override
            public void onClick(String id) {
                Intent intent = new Intent(HomeActivity.this, EventDescActivity.class);
                intent.putExtra("event_id", homeVM.getEventShortList().getValue().get(Integer.valueOf(id)));
                startActivity(intent);
            }
        });
        list.setAdapter(eventListAdapter);
        homeVM.getEventShortList().observe(this, new Observer<List<EventShort>>() {
            @Override
            public void onChanged(List<EventShort> eventShorts) {
                eventListAdapter.updateData(eventShorts);
            }
        });

        buttonFiltruj = findViewById(R.id.buttonFiltruj);
        buttonSortuj = findViewById(R.id.buttonSortuj);
        buttonWyszukaj = findViewById(R.id.buttonWyszukaj);
        linearLayout = findViewById(R.id.btnLayout);
        searchView = findViewById(R.id.searchView);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                linearLayout.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeVM.setFilteredList(newText);
                eventListAdapter.updateData(homeVM.getFilteredList());
                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.nav_home)
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            Toast.makeText(HomeActivity.this, "cos", Toast.LENGTH_SHORT).show();
        else if(R.id.nav_gallery == menuItem.getItemId())
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GalleryFragment()).commit();
            Toast.makeText(HomeActivity.this, "cos1", Toast.LENGTH_SHORT).show();
        else if(R.id.nav_cos == menuItem.getItemId())
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmet_container, new CosFragment()).commit();
            Toast.makeText(HomeActivity.this, "cos2", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (searchView.getVisibility() == View.VISIBLE) {
            hideSearchView();
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void btnWyszukaj(View view){
        linearLayout.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);

    }

    public void btnSortuj(View view){

    }

    public void btnFiltruj(View view){
        Intent intent = new Intent(HomeActivity.this, FilterActivity.class);
        startActivity(intent);
    }

    private void hideSearchView() {
        linearLayout.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
    }
}