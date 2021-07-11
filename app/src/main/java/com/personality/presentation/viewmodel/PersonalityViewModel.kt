package com.personality.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.personality.core.disposedBy
import com.personality.domain.usecase.GetCategoriesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

internal class PersonalityViewModel @Inject constructor(
    private val categoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val categoriesState = MutableLiveData<List<String>>()
    val categoriesViewState: LiveData<List<String>>
        get() = categoriesState

    fun init() {
        getCategories()
    }

    private fun getCategories() {
        categoriesUseCase.run()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccess, ::handleError)
            .disposedBy(disposable)
    }

    private fun handleSuccess(categories: List<String>) {
        categoriesState.value = categories
    }

    private fun handleError(throwable: Throwable) {}

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }


}