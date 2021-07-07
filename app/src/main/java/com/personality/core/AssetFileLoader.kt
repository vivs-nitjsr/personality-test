package com.personality.core

import android.app.Application
import java.io.InputStream
import javax.inject.Inject

internal interface AssetFileLoader {
    fun loadFileAsStream(filePath: String): InputStream
}

internal class AssetFileLoaderImpl @Inject constructor(
    private val application: Application
) : AssetFileLoader {

    override fun loadFileAsStream(filePath: String): InputStream {
        return application.assets.open(filePath)
    }
}
