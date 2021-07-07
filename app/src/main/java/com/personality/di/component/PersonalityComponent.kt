package com.personality.di.component

import android.app.Application
import com.personality.di.PersonalityApp
import com.personality.di.module.PersonalityAppModule
import com.personality.di.module.ViewModelFactoryBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelFactoryBuilder::class,
        PersonalityAppModule::class
    ]
)
internal interface PersonalityComponent : AndroidInjector<PersonalityApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun appModule(app: PersonalityApp): Builder

        fun build(): PersonalityComponent
    }
}