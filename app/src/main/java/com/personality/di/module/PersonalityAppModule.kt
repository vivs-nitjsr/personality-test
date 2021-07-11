package com.personality.di.module

import android.app.Application
import com.personality.core.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class PersonalityAppModule {

    @Provides
    @Singleton
    fun providesAssetFileLoader(application: Application): AssetFileLoader {
        return AssetFileLoaderImpl(application)
    }

    @Provides
    @Singleton
    fun providesJsonParser(): JsonParser {
        return JsonParser()
    }

    @Provides
    @Singleton
    fun providesStringLocalizer(application: Application): StringLocalizer =
        StringLocalizerImpl(application)

    @Provides
    fun providesToaster(application: Application): Toaster = AndroidToaster(application)
}