package com.aritra.notify.utils

import android.content.Context
import android.widget.Toast
import com.aritra.notify.ui.screens.MainActivity

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun Context.closeApp() {
    (this as? MainActivity)?.finish()
}