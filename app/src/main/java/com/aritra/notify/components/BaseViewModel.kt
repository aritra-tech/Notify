package com.aritra.notify.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel

class BaseViewModel : ViewModel() {

    fun openNotify(context: Context){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/aritra-tech/Notify"))
        context.startActivity(intent)
    }
}