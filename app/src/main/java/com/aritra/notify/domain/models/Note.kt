package com.aritra.notify.domain.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var note: String = "",
    var dateTime: Date? = null,
    var image: List<Uri?> = emptyList(),
    @ColumnInfo(defaultValue = "false")
    var isMovedToTrash: Boolean = false,
) : Parcelable
