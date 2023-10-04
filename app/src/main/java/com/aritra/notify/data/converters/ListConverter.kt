package com.aritra.notify.data.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ListConverter {

    private inline fun <reified T> Gson.fromJson(json: String): T =
        fromJson(json, object : TypeToken<T>() {}.type)

    @TypeConverter
    fun fromListToString(value: List<Uri?>): String {

        return Gson().toJson(value.map { it?.toString() ?: "" })
    }

    @TypeConverter
    fun fromStringToList(value: String): List<Uri?> {

        return try {
            val stringList = Gson().fromJson<List<String>>(value) //using extension function
            stringList.map { Uri.parse(it) }
        } catch (e: Exception) {
            listOf()
        }
    }
}