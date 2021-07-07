package com.personality.core

import android.app.Application
import javax.inject.Inject

internal interface AssetFileLoader {
    fun loadFileAsStream(filePath: String): String
}

internal class AssetFileLoaderImpl @Inject constructor(
    private val application: Application
) : AssetFileLoader {

    override fun loadFileAsStream(filePath: String): String {
        return application.assets.open(filePath).bufferedReader().readText()
    }
}
