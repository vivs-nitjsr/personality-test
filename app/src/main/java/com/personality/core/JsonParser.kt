package com.personality.core

import com.google.gson.Gson
import javax.inject.Inject

internal class JsonParser @Inject constructor() {
    fun <T> parse(jsonString: String, clazz: Class<T>): T? =
        try {
            Gson().fromJson(jsonString, clazz)
        } catch (e: Exception) {
            null
        }
}