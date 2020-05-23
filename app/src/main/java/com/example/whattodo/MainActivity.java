package com.example.whattodo;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.Normalizer;

import static com.example.whattodo.WhatToDo.MAINCHANNEL_ID;


public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNav;
    boolean locationActivated = false;
    String location;
    private NotificationManagerCompat notificationManager;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        notificationManager = NotificationManagerCompat.from(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new LoadingFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_sale:
                    selectedFragment = new OfertFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,
                    selectedFragment).commit();

            return true;
        }
    };

    public void setLocationActivated(boolean b) {
        this.locationActivated = b;
    }

    public boolean getLocationActivated(){
        return locationActivated;
    }

    public void setLocation(String location) {
        this.location = location;
        Log.d("SavedLocation", location);
    }

    public String getLocation(){
        return location;
    }

    public void sendOnMainChannel(View v){
        String title = "Ofertes";
        String message = "S'han afegit noves Ofertes, Fes-li una ullada!!";
        Notification notification = new NotificationCompat.Builder(this, MAINCHANNEL_ID)
                .setSmallIcon(R.drawable.bar_sale_c)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

}
