package com.personality.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.personality.R
import com.personality.core.observe
import com.personality.core.withViewModel
import com.personality.di.PersonalityApp
import com.personality.domain.model.Question
import com.personality.presentation.QuestionView
import com.personality.presentation.adapter.QuestionCategoryAdapter
import com.personality.presentation.viewmodel.PersonalityViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

internal class PersonalityTestFragment : Fragment(), HasAndroidInjector {

    companion object {
        fun newInstance() = PersonalityTestFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: PersonalityViewModel by lazy {
        withViewModel(viewModelFactory) {
            observeCategoriesViewState()
            observeQuestionViewState()
            observeCategoryViewState()
           // observeSubmitViewState()
        }
    }

    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var submitButton: Button

    private val categoriesAdapter = QuestionCategoryAdapter {
        viewModel.onCategorySelected(it)
    }

    private fun PersonalityViewModel.observeCategoriesViewState() {
        observe(categoriesViewState) { updateCategories(it) }
    }

    private fun PersonalityViewModel.observeQuestionViewState() {
        observe(questionViewState) { updateQuestion(it) }
    }

    private fun updateCategories(categories: List<String>) {
        categoriesAdapter.update(categories)
    }

    private fun PersonalityViewModel.observeCategoryViewState() {
        observe(categoryViewState) {
            categoriesAdapter.updateSelection(it)
        }
    }

    private lateinit var questionView: QuestionView

    private fun updateQuestion(question: Question) {
        with(questionView) {
            updateQuestion(question)
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        PersonalityApp.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_personality_test, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        viewModel.init()
    }

    private fun initViews(view: View) {
        val categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        categoriesRecyclerView.adapter = categoriesAdapter

        questionView = view.findViewById(R.id.questionView)

        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)
        submitButton = view.findViewById(R.id.submitButton)

        applyClickListeners()
    }

    private fun applyClickListeners() {
        previousButton.setOnClickListener { viewModel.onPreviousClicked() }
        nextButton.setOnClickListener { viewModel.onNextClicked(questionView.getAnswer()) }
    }

    override fun androidInjector() = dispatchingAndroidInjector
}