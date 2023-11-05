package com.aritra.notify

import android.app.Application
import com.aritra.notify.services.notification.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotifyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }
}
