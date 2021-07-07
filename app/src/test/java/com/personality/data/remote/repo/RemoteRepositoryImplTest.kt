package com.personality.data.remote.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.personality.QuestionsNotFoundException
import com.personality.TrampolineSchedulerRule
import com.personality.core.AssetFileLoader
import com.personality.core.JsonParser
import com.personality.data.remote.model.QuestionListResponse
import com.personality.data.remote.model.QuestionResponse
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
internal class RemoteRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val testSchedulerRule = TrampolineSchedulerRule()

    @Mock
    private lateinit var assetFileLoader: AssetFileLoader

    @Mock
    private lateinit var jsonParser: JsonParser

    private lateinit var repo: RemoteRepositoryImpl

    @Test
    fun `verify getCategories return correct observable if questions is returned`() {
        whenever(assetFileLoader.loadFileAsStream(anyString())).doReturn("JSON_TEXT")
        whenever(
            jsonParser.parse(
                "JSON_TEXT", QuestionListResponse::class.java
            )
        ).doReturn(
            QuestionListResponse(
                categories = listOf("hard_fact", "lifestyle"),
                questions = listOf()
            )
        )
        repo = RemoteRepositoryImpl(assetFileLoader, jsonParser)
        val actualResult = repo.getCategories().test()

        actualResult.assertNoErrors()
        actualResult.assertValue(listOf("hard_fact", "lifestyle"))
    }

    @Test
    fun `verify getCategories return error is thrown if questions is not returned`() {
        whenever(assetFileLoader.loadFileAsStream(anyString())).doReturn("JSON_TEXT")
        whenever(
            jsonParser.parse(
                "JSON_TEXT", QuestionListResponse::class.java
            )
        ).doReturn(null)

        repo = RemoteRepositoryImpl(assetFileLoader, jsonParser)

        val actualResult = repo.getCategories().test()
        actualResult.assertError(QuestionsNotFoundException)
    }

    @Test
    fun `verify getQuestions apply correct filter with category`() {
        whenever(assetFileLoader.loadFileAsStream(anyString())).doReturn("JSON_TEXT")
        whenever(
            jsonParser.parse(
                "JSON_TEXT", QuestionListResponse::class.java
            )
        ).doReturn(
            QuestionListResponse(
                categories = listOf("hard_fact", "lifestyle"),
                questions = listOf(mockQuestionResponse)
            )
        )

        repo = RemoteRepositoryImpl(assetFileLoader, jsonParser)

        val actualResult = repo.getQuestions("hard_fact").test()
        actualResult.assertNoErrors()
        actualResult.assertValue(listOf(mockQuestionResponse))
    }

    @Test
    fun `verify getQuestions return empty if category is not available`() {
        whenever(assetFileLoader.loadFileAsStream(anyString())).doReturn("JSON_TEXT")
        whenever(
            jsonParser.parse(
                "JSON_TEXT", QuestionListResponse::class.java
            )
        ).doReturn(
            QuestionListResponse(
                categories = listOf("hard_fact", "lifestyle"),
                questions = listOf(mockQuestionResponse)
            )
        )

        repo = RemoteRepositoryImpl(assetFileLoader, jsonParser)

        val actualResult = repo.getQuestions("lifestyle").test()
        actualResult.assertNoErrors()
        actualResult.assertValue(listOf())
    }

    @Test
    fun `verify error is thrown if questions is not available when getQuestions is called`() {
        whenever(assetFileLoader.loadFileAsStream(anyString())).doReturn("JSON_TEXT")
        whenever(
            jsonParser.parse(
                "JSON_TEXT", QuestionListResponse::class.java
            )
        ).doReturn(null)

        repo = RemoteRepositoryImpl(assetFileLoader, jsonParser)

        val actualResult = repo.getQuestions("hard_fact").test()
        actualResult.assertError(QuestionsNotFoundException)
    }

    private val mockQuestionResponse = QuestionResponse(
        question = "Question",
        category = "hard_fact",
        questionType = mock()
    )
}