package com.aritra.notify.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns

fun <T> Context.getFileInfo(file: Uri, columnName: String = OpenableColumns.DISPLAY_NAME, data: Cursor.(Int) -> T) =
    contentResolver?.query(file, null, null, null, null)?.run {
        val index = getColumnIndex(columnName)
        moveToFirst()
        val result = data(index)
        close()
        result
    }
