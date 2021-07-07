package com.personality.core

import com.google.gson.Gson

internal class JsonParser {
    fun <T> parse(jsonString: String, clazz: Class<T>): T? =
        try {
            Gson().fromJson(jsonString, clazz)
        } catch (e: Exception) {
            null
        }
}