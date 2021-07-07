package com.personality.di.module

import com.personality.core.AssetFileLoader
import com.personality.core.JsonParser
import com.personality.data.remote.repo.RemoteRepositoryImpl
import com.personality.domain.repo.RemoteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class RepoModule {

    @Provides
    @Singleton
    fun providesRemoteRepository(
        assetFileLoader: AssetFileLoader,
        jsonParser: JsonParser
    ): RemoteRepository {
        return RemoteRepositoryImpl(
            assetFileLoader = assetFileLoader,
            jsonParser = jsonParser
        )
    }
}
