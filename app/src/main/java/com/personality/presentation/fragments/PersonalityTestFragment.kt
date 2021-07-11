package com.personality.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.personality.R
import com.personality.core.observe
import com.personality.core.withViewModel
import com.personality.di.PersonalityApp
import com.personality.presentation.categories.QuestionCategoryAdapter
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
        }
    }

    private val categoriesAdapter = QuestionCategoryAdapter()

    private fun PersonalityViewModel.observeCategoriesViewState() {
        observe(categoriesViewState) { updateCategories(it) }
    }

    private fun updateCategories(categories: List<String>) {
        categoriesAdapter.update(categories)
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
    }

    override fun androidInjector() = dispatchingAndroidInjector
}