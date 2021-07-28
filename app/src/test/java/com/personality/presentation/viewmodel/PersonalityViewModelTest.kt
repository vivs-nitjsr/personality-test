package com.personality.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.personality.TrampolineSchedulerRule
import com.personality.core.StringLocalizer
import com.personality.core.Toaster
import com.personality.domain.model.Question
import com.personality.domain.model.QuestionType
import com.personality.domain.usecase.GetCategoriesUseCase
import com.personality.domain.usecase.GetQuestionsUseCase
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
internal class PersonalityViewModelTest {

    companion object {
        private const val TEST_ANSWER = 0
    }

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var schedulerRule = TrampolineSchedulerRule()

    private lateinit var viewModel: PersonalityViewModel

    @Mock
    private lateinit var toaster: Toaster

    @Mock
    private lateinit var stringLocalizer: StringLocalizer

    @Mock
    private lateinit var categoriesUseCase: GetCategoriesUseCase

    @Mock
    private lateinit var getQuestionsUseCase: GetQuestionsUseCase


    private val question1 = Question(
        question = "How are you?",
        category = "category1",
        questionType = mock()
    )
    private val question2 = Question(
        question = "Where are you?",
        category = "category1",
        questionType = mock()
    )

    private val questionsList = listOf(
        question1,
        question2
    )

    @Before
    fun setUp() {
        viewModel = PersonalityViewModel(
            toaster = toaster,
            stringLocalizer = stringLocalizer,
            categoriesUseCase = categoriesUseCase,
            getQuestionsUseCase = getQuestionsUseCase
        )
    }

    @Test
    fun `verify init is called first question is loaded`() {
        whenever(categoriesUseCase.run()).doReturn(Observable.just(listOf("category1")))
        whenever(getQuestionsUseCase.run("category1")).doReturn(Observable.just(questionsList))

        viewModel.init()
        assertEquals(listOf("category1"), viewModel.categoriesViewState.value)
        assertEquals(question1, viewModel.questionViewState.value)
    }

    @Test
    fun `verify next is clicked advance to the next question`() {
        whenever(categoriesUseCase.run()).doReturn(Observable.just(listOf("category1")))
        whenever(getQuestionsUseCase.run("category1")).doReturn(Observable.just(questionsList))

        viewModel.init()
        viewModel.onNextClicked(TEST_ANSWER)

        assertEquals(question2, viewModel.questionViewState.value)

    }

    @Test
    fun `verify next button works for last questions and next category is selected`() {
        whenever(categoriesUseCase.run()).doReturn(
            Observable.just(
                listOf("category1", "category2")
            )
        )
        whenever(getQuestionsUseCase.run(anyString())).doReturn(Observable.just(questionsList))

        viewModel.init()
        viewModel.onNextClicked(TEST_ANSWER)
        viewModel.onNextClicked(TEST_ANSWER)

        assertEquals("category2", viewModel.categoryViewState.value)
        assertEquals(question1, viewModel.questionViewState.value)
    }

    @Test
    fun `verify next button should not change the question on the last question of last category`() {
        whenever(categoriesUseCase.run()).doReturn(
            Observable.just(
                listOf("category1")
            )
        )
        whenever(getQuestionsUseCase.run(anyString())).doReturn(Observable.just(questionsList))
        viewModel.init()
        viewModel.onNextClicked(TEST_ANSWER)
        viewModel.onNextClicked(TEST_ANSWER)

        assertEquals(question2, viewModel.questionViewState.value)

    }

    @Test
    fun `verify previous button click goes to previous question`() {
        whenever(categoriesUseCase.run()).doReturn(Observable.just(listOf("category1")))
        whenever(getQuestionsUseCase.run("category1")).doReturn(Observable.just(questionsList))

        viewModel.init()
        viewModel.onNextClicked(TEST_ANSWER)

        viewModel.onPreviousClicked()
        assertEquals(question1, viewModel.questionViewState.value)
    }

    @Test
    fun `verify previous button clicked on first question then category is changed and last question is selected`() {
        whenever(categoriesUseCase.run()).doReturn(
            Observable.just(
                listOf(
                    "category1",
                    "category2"
                )
            )
        )
        whenever(getQuestionsUseCase.run(anyString())).doReturn(Observable.just(questionsList))

        viewModel.init()
        viewModel.onNextClicked(TEST_ANSWER)
        viewModel.onNextClicked(TEST_ANSWER).also {
            assertEquals("category2", viewModel.categoryViewState.value)
            assertEquals(question1, viewModel.questionViewState.value)
        }

        viewModel.onPreviousClicked()
        assertEquals("category1", viewModel.categoryViewState.value)
        assertEquals(question2, viewModel.questionViewState.value)
    }

    @Test
    fun `verify previous button clicked on first question question is not changed`() {
        whenever(categoriesUseCase.run()).doReturn(
            Observable.just(
                listOf(
                    "category1",
                )
            )
        )
        whenever(getQuestionsUseCase.run(anyString())).doReturn(Observable.just(questionsList))

        viewModel.init()

        viewModel.onPreviousClicked()
        assertEquals(question1, viewModel.questionViewState.value)
    }
}