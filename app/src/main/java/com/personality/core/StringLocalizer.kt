package com.personality.core

import android.app.Application
import androidx.annotation.StringRes
import javax.inject.Inject

internal interface StringLocalizer {
    fun getText(@StringRes resId: Int): String
}

internal class StringLocalizerImpl @Inject constructor(
    private val application: Application
) : StringLocalizer {

    override fun getText(resId: Int): String {
        return application.getString(resId)
    }
}