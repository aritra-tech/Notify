package com.aritra.notify.data.converters

import android.os.Build
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

object LocalDateTimeConverter {

    @TypeConverter
    @JvmStatic
    fun toDate(value: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(value,0, ZoneOffset.UTC)
    }

    @TypeConverter
    @JvmStatic
    fun toString(date: LocalDateTime): Long {
        return date.toEpochSecond(ZoneOffset.UTC)
    }
}