package com.example.drivefest;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drivefest.viewmodel.HomeViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private HomeViewModel homeVM;
    private DrawerLayout drawerLayout;

    private HashMap<String, List<String>> checkedItems;
    private String startDate, endDate, sortBy;
    private ActivityResultLauncher<Intent> filterActivityResultLauncher;
    private ActivityResultLauncher<Intent> sortActivityResultLauncher;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private boolean mToolBarNavigationListenerIsRegistered = false;
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.home_drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)));


        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(toggle);
        //toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            toolbar.setPadding(0, topInset, 0, 0);
            return insets;
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        homeVM = new ViewModelProvider(this).get(HomeViewModel.class);
        homeVM.fetchEventShortList();
        homeVM.fetchFavEventShortList();

        //homeVM.updateUserProfile("Nazwa testowa", "https://firebasestorage.googleapis.com/v0/b/moto-event.appspot.com/o/images%2Fusers%2Fdefault.jpg?alt=media&token=98a3e130-f318-4581-9a71-21fa18755eba");
        Log.e("deeegbbubbug", homeVM.getUsername());
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new EventsFragment(), "eventsFragment")
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.nav_events)
            ;
        if(R.id.nav_followed == menuItem.getItemId()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new FavoritesFragment(), "favoriteFragment")
                    .addToBackStack("favorites")
                    .commit();
        }
        else if(R.id.nav_workshop == menuItem.getItemId()) {
            homeVM.fetchWorkshopsList();
            homeVM.fetchRatedWorkshops();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new WorkshopsFragment(), "workshopsFragment")
                    .addToBackStack("workshops")
                    .commit();
        }
        else if(R.id.nav_workshop == menuItem.getItemId()) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmet_container, new CosFragment()).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //przy add fragment ise zapamietuje a przy replace jest niszczony
        //zrobic sprawdzenie fragmentu
        //nie dzilaa w searchview przez hide w sort
/*
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.container);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else if (fragment != null) {
            if (fragment instanceof EventsFragment) {
                EventsFragment fr = (EventsFragment) fragment;
                fr.hideSearchView();
                return;
            } else*/
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void setupDrawerToggle(boolean val) {
        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(toggle);
        //toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        if (val) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                toggle.setToolbarNavigationClickListener(v -> onBackPressed());


        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            toggle.syncState();

        }

    }
}