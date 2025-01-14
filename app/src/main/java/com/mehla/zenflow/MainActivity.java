package com.mehla.zenflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.mehla.zenflow.databinding.ActivityMainBinding;
import com.mehla.zenflow.services.DarkMode;
import com.mehla.zenflow.services.FirebaseService;
import com.mehla.zenflow.ui.stopwatch.Stopwatch;
import com.mehla.zenflow.ui.timer.Timer;
import com.mehla.zenflow.ui.workouts.WorkoutsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WorkoutsFragment workoutsFragment;

    private NavController navController;

    DarkMode dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        workoutsFragment = new WorkoutsFragment();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_workouts, R.id.navigation_settings)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_dashboard) {
                    // Navigate to the DashboardFragment
                    navController.navigate(R.id.navigation_dashboard);
                    return true;
                } else if (itemId == R.id.navigation_home) {
                    // Navigate to the HomeFragment
                    navController.navigate(R.id.navigation_home);
                    return true;
                } else if (itemId == R.id.navigation_workouts) {
                    // Navigate to the WorkoutsFragment
                    navController.navigate(R.id.navigation_workouts);
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    // Navigate to the SettingsFragment
                    navController.navigate(R.id.navigation_settings);
                    return true;
                }
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });


        // Firebase
        FirebaseService fbs = new FirebaseService();

        dm = new DarkMode(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        // Find the NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Get the current destination
        NavDestination currentDestination = navController.getCurrentDestination();

        // Check if the current destination corresponds to WorkoutsFragment
        if (currentDestination != null && currentDestination.getId() == R.id.navigation_workouts) {
            inflater.inflate(R.menu.workouts_menu, menu);
        } else {
            inflater.inflate(R.menu.home_options_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.option_menu_timer) {
            Intent intent = new Intent(this, Timer.class);
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.option_menu_stopwatch) {
            Intent intent = new Intent(this, Stopwatch.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            navController.navigate(R.id.navigation_dashboard);
            return true;
        } else if (item.getItemId() == R.id.action_add) {
            addExercise();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void updateMenu() {
        // This will show the menu
        invalidateOptionsMenu();
    }

    public void addExercise() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        Fragment currentFragment = null;
        if (navHostFragment != null) {
            currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        }

        if (currentFragment != null && currentFragment instanceof WorkoutsFragment) {
            ((WorkoutsFragment) currentFragment).addExercise();
        } else {
            Log.e("MainActivity", "WorkoutsFragment is not the current fragment!");
        }
    }

    @Override
    public void onBackPressed() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        Fragment currentFragment = null;
        if (navHostFragment != null) {
            currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        }

        if (currentFragment != null && currentFragment.getClass().getSimpleName().equals("ExerciseDetails")) {
            // Navigate to DashboardFragment
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_dashboard);
        } else {
            // Navigate to Home or call super
            super.onBackPressed();
        }
    }




}