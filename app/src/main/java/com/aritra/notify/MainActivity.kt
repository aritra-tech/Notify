package com.aritra.notify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aritra.notify.navigation.NotifyApp
import com.aritra.notify.ui.theme.NotifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }
        setContent {
            NotifyTheme {
                NotifyApp(onLoaded = {
                    keepSplashOpened = false
                })
            }
        }
    }
}
