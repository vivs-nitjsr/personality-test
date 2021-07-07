package com.personality.di.module

import android.app.Application
import com.personality.core.AssetFileLoader
import com.personality.core.AssetFileLoaderImpl
import com.personality.core.JsonParser
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
    fun providesJsonParser() : JsonParser {
        return JsonParser()
    }
}