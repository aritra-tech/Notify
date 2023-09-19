package com.aritra.notify.data.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.aritra.notify.data.converters.UriConverter
import com.aritra.notify.utils.DateTypeConverter
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "note")
@TypeConverters(DateTypeConverter::class, UriConverter::class)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var note: String,
    var dateTime: Date?,
    var image: Uri?
) : Parcelable