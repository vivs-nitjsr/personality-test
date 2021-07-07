package com.personality.di.module

import androidx.lifecycle.ViewModel
import com.personality.core.ViewModelKey
import com.personality.presentation.viewmodel.PersonalityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelBuilder {
    @Binds
    @IntoMap
    @ViewModelKey(PersonalityViewModel::class)
    abstract fun bindPersonalityViewModel(viewModel: PersonalityViewModel): ViewModel

}
