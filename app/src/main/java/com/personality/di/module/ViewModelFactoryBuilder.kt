package com.personality.di.module

import androidx.lifecycle.ViewModelProvider
import com.personality.core.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        ViewModelBuilder::class
    ]
)
internal abstract class ViewModelFactoryBuilder {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}