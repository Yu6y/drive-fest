package com.example.drivefest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private HomeViewModel homeVM;
    private EventListAdapter eventListAdapter;
    private RecyclerView list;
    private DrawerLayout drawerLayout;
    private LinearLayout linearLayout;
    private SearchView searchView;
    private HashMap<String, List<String>> checkedItems;
    private String startDate, endDate, sortBy;
    private ActivityResultLauncher<Intent> filterActivityResultLauncher;
    private ActivityResultLauncher<Intent> sortActivityResultLauncher;
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
                list.scrollToPosition(0);
                homeVM.setFilteredList(newText, null, null, null);
                eventListAdapter.updateData(homeVM.getFilteredList());
                return true;
            }
        });

        filterActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        checkedItems = (HashMap<String, List<String>>) data.getSerializableExtra("checkedItems");
                        startDate = data.getSerializableExtra("startDate").toString();
                        endDate = data.getSerializableExtra("endDate").toString();

                        homeVM.setFilteredList(null, startDate, endDate, checkedItems);
                        eventListAdapter.updateData(homeVM.getFilteredList());
                        list.scrollToPosition(0);
                    }

                }
        );
        sortActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        sortBy = data.getSerializableExtra("sort").toString();
                        eventListAdapter.updateSortCriteria(sortBy);
                        if(sortBy.equals("xdefault")) {
                            if (homeVM.getFilteredList().isEmpty()) {
                                Log.d("debug list orig", homeVM.getEventShortList().getValue().get(1).getName());
                                eventListAdapter.updateData(homeVM.getEventShortList().getValue());
                            }
                            else {
                                Log.d("debug list", homeVM.getFilteredList().get(1).getName());
                                eventListAdapter.updateData(homeVM.getFilteredList());
                            }
                        }
                        list.scrollToPosition(0);
                    }
                }
        );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        /*if(menuItem.getItemId() == R.id.nav_events)
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            onBackPressed();*/
        if(R.id.nav_followed == menuItem.getItemId()) {

        }
        else if(R.id.nav_workshop == menuItem.getItemId()) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmet_container, new CosFragment()).commit();
        }
        else if(R.id.nav_workshop == menuItem.getItemId()) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmet_container, new CosFragment()).commit();
        }
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
        Intent intent = new Intent(HomeActivity.this, SortActivity.class);

        if(sortBy != null){
            intent.putExtra("sort", sortBy);
        }
        sortActivityResultLauncher.launch(intent);

    }

    public void btnFiltruj(View view){
        Intent intent = new Intent(HomeActivity.this, FilterActivity.class);

        if(startDate != null && endDate != null && checkedItems != null) {
            Log.e("deeebuug", "csa");
            intent.putExtra("checkedItems", (Serializable) checkedItems);
            intent.putExtra("startDate", (Serializable) startDate);
            intent.putExtra("endDate", (Serializable) endDate);
        }
        filterActivityResultLauncher.launch(intent);
    }

    private void hideSearchView() {
        linearLayout.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
    }
}