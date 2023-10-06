package com.aritra.notify.data.converters

import android.net.Uri
import androidx.room.TypeConverter

object UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(string: String?): Uri? {
        if (string == null) {
            return null
        }

        return Uri.parse(string)
    }
}
