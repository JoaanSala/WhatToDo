package com.example.whattodo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class WhatToDo : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mainChannel = NotificationChannel(
                    MAINCHANNEL_ID,
                    "Main Chanel",
                    NotificationManager.IMPORTANCE_HIGH
            )
            mainChannel.description = "This is MainChannel"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(mainChannel)
        }
    }

    companion object {
        const val MAINCHANNEL_ID = "Main Chanel"
    }
}