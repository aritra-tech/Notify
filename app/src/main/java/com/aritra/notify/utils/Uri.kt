package com.aritra.notify.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toFile
import java.io.File

/**
 * Converts an android content uri to a file.
 */
fun Uri.toFile(context: Context): File? {
    if (!exists()) return null

    return try {
        toFile()
    } catch (e: IllegalArgumentException) {
        context.getFileInfo(this) {
            File(getString(it))
        }
    }
}

/**
 * Checks if an android content uri exists.
 */
fun Uri.exists(): Boolean {
    return try {
        toFile().exists()
    } catch (e: IllegalArgumentException) {
        val path = path?.replace(
            "external_files",
            Environment.getExternalStorageDirectory().toString()
        ).toString()
        File(path).exists()
    }
}
