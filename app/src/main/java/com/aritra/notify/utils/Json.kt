package com.aritra.notify.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String?): T? {
    if (json == null) {
        return null
    }
    return this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}

inline fun <reified T> Gson.toString(data: T?): String? {
    if (data == null) {
        return null
    }
    return toJson(data)
}
