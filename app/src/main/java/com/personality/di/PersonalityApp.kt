package com.personality.di

import android.app.Application
import com.personality.di.component.DaggerPersonalityComponent
import com.personality.di.component.PersonalityComponent
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

internal object PersonalityApp {

    @set:Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    fun initAndInject(application: Application): PersonalityComponent {
        return DaggerPersonalityComponent.builder()
            .application(application)
            .appModule(this)
            .build()
            .also {
                it.inject(this)
            }
    }

    fun inject(resources: Any) {
        dispatchingAndroidInjector.inject(resources)
    }
}