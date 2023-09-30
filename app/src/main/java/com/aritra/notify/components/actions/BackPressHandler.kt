package com.aritra.notify.components.actions

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.aritra.notify.R
import com.aritra.notify.utils.closeApp
import com.aritra.notify.utils.toast
import kotlinx.coroutines.delay


@Composable
fun BackPressHandler() {
    var exit by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = exit) {
        if (exit) {
            delay(2_000L)
            exit = false
        }
    }

    BackHandler(enabled = true) {
        if (exit) {
            context.closeApp()
        } else {
            exit = true
            context.toast(context.getText(R.string.press_again_to_exit).toString())
        }
    }

}