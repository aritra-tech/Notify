package com.aritra.notify.services.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build

fun createNotificationChannel(
    context: Context,
) {
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    if (Build.VERSION.SDK_INT >= 26) {
        NotificationChannel("1", "ReminderNote", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Reminder Notes"
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)
            notificationManager.createNotificationChannel(this)
        }
    }
}
