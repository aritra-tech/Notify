package com.aritra.notify.data.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.aritra.notify.domain.models.Todo
import com.aritra.notify.utils.fromJson
import com.aritra.notify.utils.toString
import com.google.gson.Gson

object CollectionConverter {

    @TypeConverter
    fun fromUriListToString(value: List<Uri?>): String {
        return Gson().toJson(value.map { it?.toString() ?: "" })
    }

    @TypeConverter
    fun fromStringToUriList(value: String): List<Uri?> {
        return try {
            val stringList = Gson().fromJson<List<String>>(value) // using extension function
            stringList?.map { Uri.parse(it) } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromTodoListToString(value: List<Todo>?): String? {
        return Gson().toString(value)
    }

    @TypeConverter
    fun fromStringToTodoList(value: String?): List<Todo>? {
        return Gson().fromJson(value)
    }
}
