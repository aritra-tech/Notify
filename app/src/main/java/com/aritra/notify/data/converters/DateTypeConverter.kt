package com.aritra.notify.data.converters

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import com.aritra.notify.utils.Const
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTypeConverter {
    @SuppressLint("ConstantLocale")
    private val displayDateFormat = SimpleDateFormat(Const.DATE_TIME_FORMAT, Locale.getDefault())

    @TypeConverter
    @JvmStatic
    fun toDate(value: String?): Date? {
        return if (value.isNullOrEmpty()) {
            null
        } else {
            displayDateFormat.parse(value)
        }
    }

    @TypeConverter
    @JvmStatic
    fun toString(date: Date?): String? {
        return date?.let { displayDateFormat.format(date) }
    }
}
