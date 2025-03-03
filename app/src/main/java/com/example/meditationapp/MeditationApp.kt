package com.example.meditationapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MeditationApp : Application() {
    companion object {
        const val MEDITATION_CHANNEL_ID = "meditation_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Meditation"
            val descriptionText = "Meditation player notifications"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(MEDITATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
} 