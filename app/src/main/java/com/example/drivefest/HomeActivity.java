package com.example.drivefest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.drivefest.viewmodel.CarRegisterViewModel;
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
    private CarRegisterViewModel carVM;
    private DrawerLayout drawerLayout;
    private HashMap<String, List<String>> checkedItems;
    private String startDate, endDate, sortBy;
    private ActivityResultLauncher<Intent> filterActivityResultLauncher;
    private ActivityResultLauncher<Intent> sortActivityResultLauncher;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private final String SHARED_PREFS = "shared_prefs";

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
        carVM = new ViewModelProvider(this).get(CarRegisterViewModel.class);
        homeVM.fetchEventShortList();
        homeVM.fetchFavEventShortList();

        Log.e("deeegbbubbug", homeVM.getUserDisplayName());
        Log.e("deeeebuug", homeVM.getUserPic());
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new EventsFragment(), "eventsFragment")
                    .addToBackStack(null)
                    .commit();
        }
        updateNav();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

        if(R.id.nav_events == menuItem.getItemId()) {
            if (currentFragment != null && currentFragment.getTag() != null && currentFragment.getTag().equals("eventsFragment")) {
                // eventsFragment już jest widoczny, nic nie rób
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            } else {
                // Ukryj aktualny fragment i usuń go ze stosu
                if (currentFragment != null) {
                    fragmentManager.beginTransaction()
                            .hide(currentFragment)
                            .commit();
                    fragmentManager.popBackStack();
                }

            }
        }
        else if(R.id.nav_followed == menuItem.getItemId()) {
            homeVM.fetchFavEventShortList();
            //sprawdzic czy jest na stacku jesli tak to wyjebac i odpalic
            //onback ogarnac
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
        else if(R.id.nav_user == menuItem.getItemId()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new UserFragment(), "userFragment")
                    .addToBackStack("register")
                    .commit();
        }
        else if(R.id.nav_add_event == menuItem.getItemId()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new AddEventFragment(), "addEventFragment")
                    .addToBackStack("addEvent")
                    .commit();
        }
        else if(R.id.nav_add_workshop == menuItem.getItemId()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new AddWorkshopFragment(), "addWorkshopFragment")
                    .addToBackStack("addWorkshop")
                    .commit();
        }
        else if(R.id.nav_logout == menuItem.getItemId()){
            SharedPreferences sharedPreferences;
            sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (backStackEntryCount > 1)
            fragmentManager.popBackStack();
        else if (backStackEntryCount == 1)
            ;
        else
            super.onBackPressed();

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

    private void updateNav(){
        NavigationView nav = findViewById(R.id.nav_view);
        View header = nav.getHeaderView(0);
        TextView name = header.findViewById(R.id.nav_name);
        ImageView image = header.findViewById(R.id.nav_image);

        name.setText(homeVM.getUserDisplayName());
        Glide
                .with(this)
                .load(homeVM.getUserPic())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.ic_cloud_download)
                .error(R.drawable.ic_error)
                .into(image);
    }
}