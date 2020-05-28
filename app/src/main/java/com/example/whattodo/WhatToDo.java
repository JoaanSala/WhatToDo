package com.example.whattodo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class WhatToDo extends Application {
    public static final String MAINCHANNEL_ID = "Main Chanel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mainChannel = new NotificationChannel(
                    MAINCHANNEL_ID,
                    "Main Chanel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            mainChannel.setDescription("This is MainChannel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(mainChannel);

        }
    }
}
