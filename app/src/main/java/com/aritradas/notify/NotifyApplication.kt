package com.aritradas.notify

import android.app.Application
import com.aritradas.notify.services.notification.createNotificationChannel
import com.aritradas.notify.utils.AppShortcuts
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotifyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // creates the notification channel
        createNotificationChannel(this)

        // Adds the shortcuts
        AppShortcuts.showShortCuts(this)
    }
}
