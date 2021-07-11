package com.personality.di.module

import com.personality.presentation.activities.PersonalityActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun bindPersonalityActivity(): PersonalityActivity
}