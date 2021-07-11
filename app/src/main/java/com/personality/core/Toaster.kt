package com.personality.core

import android.app.Application
import android.widget.Toast
import javax.inject.Inject

internal interface Toaster {
    fun toast(message: String)
}

internal class AndroidToaster @Inject constructor(
    private val application: Application
): Toaster {

    override fun toast(message: String) {
       Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
    }

}