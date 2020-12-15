package com.kom5a_tugasbesar.selfood.Activity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kom5a_tugasbesar.selfood.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view_resto);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_resto, R.id.navigation_dashboard_resto)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_resto);
        NavigationUI.setupWithNavController(navView, navController);
    }

}