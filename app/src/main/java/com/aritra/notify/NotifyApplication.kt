package com.aritra.notify

import android.app.Application
import com.aritra.notify.services.notification.createNotificationChannel
import com.aritra.notify.utils.AppShortcuts
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotifyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //creates the notification channel
        createNotificationChannel(this)

        // Adds the shortcuts
        AppShortcuts.showShortCuts(this)
    }
}
