package com.personality.di.module

import com.personality.presentation.fragments.PersonalityTestFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindsPersonalityFragment(): PersonalityTestFragment
}
