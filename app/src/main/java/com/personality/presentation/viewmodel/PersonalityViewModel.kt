package com.personality.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.personality.R
import com.personality.core.StringLocalizer
import com.personality.core.Toaster
import com.personality.core.disposedBy
import com.personality.domain.model.Question
import com.personality.domain.usecase.GetCategoriesUseCase
import com.personality.domain.usecase.GetQuestionsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

internal class PersonalityViewModel @Inject constructor(
    private val toaster: Toaster,
    private val stringLocalizer: StringLocalizer,
    private val categoriesUseCase: GetCategoriesUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val disposeBag = CompositeDisposable()

    private val categoriesState = MutableLiveData<List<String>>()
    val categoriesViewState: LiveData<List<String>>
        get() = categoriesState

    private val questionState = MutableLiveData<Question>()
    val questionViewState: LiveData<Question>
        get() = questionState

    private val submitState = MutableLiveData<Boolean>()
    val submitViewState: LiveData<Boolean>
        get() = submitState

    private var selectedCategory = ""

    private val questions = mutableListOf<Question>()
    private var selectedQuestion = 0

    fun init() {
        getCategories()
    }

    private fun getCategories() {
        categoriesUseCase.run()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccess, ::handleError)
            .disposedBy(disposeBag)
    }

    private fun handleSuccess(categories: List<String>) {
        if (categories.isNotEmpty()) {
            selectedCategory = categories[0]
            categoriesState.value = categories

            getQuestionsByCategories(selectedCategory)
        }
    }

    private fun getQuestionsByCategories(category: String) {
        getQuestionsUseCase.run(category)
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handleQuestionListSuccess(it) },
                ::handleQuestionListFailure
            ).disposedBy(disposeBag)
    }

    fun onCategorySelected(category: String) {
        getQuestionsByCategories(category)
    }

    private fun handleQuestionListSuccess(questions: List<Question>) {
        this.questions.clear()
        this.questions.addAll(questions)
        resetQuestionView()

        loadQuestion()
    }

    private fun resetQuestionView() {
        selectedQuestion = 0
        submitState.value = false
    }

    private fun loadQuestion() {
        questionState.value = questions[selectedQuestion]
    }

    private fun handleQuestionListFailure(throwable: Throwable) {

    }

    private fun showLoading() {

    }

    private fun hideLoading() {

    }

    private fun handleError(throwable: Throwable) {}

    override fun onCleared() {
        disposeBag.clear()
        super.onCleared()
    }

    fun onSubmitClicked(question: Question, answer: Int) {
        // TODO: Selection persistence
        if (selectedQuestion < questions.size) {
            selectedQuestion++
            val isLastQuestion = selectedQuestion >= questions.size - 1
            submitState.value = isLastQuestion
            if (selectedQuestion < questions.size) {
                loadQuestion()
            }
        }
    }

    fun onError() {
        toaster.toast(stringLocalizer.getText(R.string.select_choice))
    }
}