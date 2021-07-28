package com.personality.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.personality.R
import com.personality.core.StringLocalizer
import com.personality.core.Toaster
import com.personality.core.disposedBy
import com.personality.domain.model.Answer
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

    private val categoryState = MutableLiveData<String>()
    val categoryViewState: LiveData<String> get() = categoryState

    private var selectedCategory = ""

    private val questions = mutableListOf<Question>()
    private var selectedQuestion = 0

    private var categoryIndex = 0

    fun init() {
        getCategories()
    }

    private val solutionMap: MutableMap<Question, Answer> = mutableMapOf()

    private fun getCategories() {
        categoriesUseCase.run()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleSuccess, ::handleError)
            .disposedBy(disposeBag)
    }

    private fun handleSuccess(categories: List<String>) {
        if (categories.isNotEmpty()) {
            selectedCategory = categories[categoryIndex]
            categoriesState.value = categories

            getQuestionsByCategories(selectedCategory)
        }
    }

    private fun getQuestionsByCategories(category: String, reverse: Boolean = false) {
        getQuestionsUseCase.run(category)
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handleQuestionListSuccess(it, reverse) },
                ::handleQuestionListFailure
            ).disposedBy(disposeBag)
    }

    fun onCategorySelected(category: String, reverse: Boolean = false) {
        getQuestionsByCategories(category, reverse)
    }

    private fun handleQuestionListSuccess(questions: List<Question>, reverse: Boolean = false) {
        this.questions.clear()
        this.questions.addAll(questions)
        selectedQuestion = if (reverse) {
            this.questions.size - 1
        } else {
            0
        }
        submitState.value = false

        loadQuestion()
    }

    private fun findNextUnansweredQuestion() =
        this.questions.indexOfFirst { solutionMap[it] == null }

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

    fun onNextClicked(answer: Int?) {
        if (answer == null || questionState.value == null) {
            onError()

            return
        }

        val question = questionState.value!!
        solutionMap[question] = Answer(answer.toString())

        selectedQuestion++

        if (selectedQuestion < questions.size) {
            loadQuestion()
        } else {
            selectNextCategory()
        }
    }

    private fun selectNextCategory() {
        selectedQuestion = 0
        categoryIndex++
        val category = categoriesState.value
        if (category != null && categoryIndex < category.size) {
            selectedCategory = category[categoryIndex]
            categoryState.value = selectedCategory
            onCategorySelected(selectedCategory)
        }
    }

    fun onPreviousClicked() {
        selectedQuestion--
        if (selectedQuestion >= 0) {
            loadQuestion()
        } else {
            categoryIndex--
            val category = categoriesState.value
            if (category != null && categoryIndex >= 0) {
                selectedCategory = category[categoryIndex]
                categoryState.value = selectedCategory
                onCategorySelected(selectedCategory, reverse = true)
            }
        }
    }

    fun onError() {
        toaster.toast(stringLocalizer.getText(R.string.select_choice))
    }
}