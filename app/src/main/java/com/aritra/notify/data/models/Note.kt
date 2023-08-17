package com.aritra.notify.data.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.aritra.notify.utils.DateTypeConverter
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream
import java.util.Date

@Parcelize
@Entity(tableName = "note")
@TypeConverters(DateTypeConverter::class)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var note: String,
    var dateTime: Date?,
    @TypeConverters(BitmapConverters::class)
    var imagePath: Bitmap?
) : Parcelable
class BitmapConverters {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream)
            return outputStream.toByteArray()
        }
        return null
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        return if (byteArray != null)
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        else null
    }
}