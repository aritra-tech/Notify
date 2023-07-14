package com.aritra.notify.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aritra.notify.utils.DateTypeConverter
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "note")
@TypeConverters(DateTypeConverter::class)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var note: String,
    var dateTime: Date?
) : Parcelable
