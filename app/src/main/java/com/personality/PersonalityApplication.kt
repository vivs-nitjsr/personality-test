package com.personality

import android.app.Application
import com.personality.di.PersonalityApp
import com.personality.di.component.PersonalityComponent

class PersonalityApplication : Application() {

    private lateinit var appComponent: PersonalityComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = PersonalityApp.initAndInject(this)
    }
}