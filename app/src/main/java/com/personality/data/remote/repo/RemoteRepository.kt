package com.personality.data.remote.repo

import com.personality.QuestionsNotFoundException
import com.personality.core.AssetFileLoader
import com.personality.core.JsonParser
import com.personality.data.remote.model.QuestionListResponse
import com.personality.data.remote.model.QuestionResponse
import com.personality.domain.repo.RemoteRepository
import io.reactivex.Observable

internal class RemoteRepositoryImpl constructor(
    private val assetFileLoader: AssetFileLoader,
    private val jsonParser: JsonParser
) : RemoteRepository {
    private var questionsList: QuestionListResponse? = null

    override fun getCategories(): Observable<List<String>> {
        return Observable.create { emitter ->
            val questionListResponse = getQuestionsList()

            if (questionListResponse == null) {
                emitter.onError(QuestionsNotFoundException)
            } else {
                emitter.onNext(questionListResponse.categories)
            }
        }
    }

    override fun getQuestions(category: String): Observable<List<QuestionResponse>> {
        return Observable.create { emitter ->
            val questionListResponse = getQuestionsList()

            if (questionListResponse == null) {
                emitter.onError(QuestionsNotFoundException)
            } else {
                emitter.onNext(
                    questionListResponse.questions.filter {
                        it.category == category
                    }
                )
            }
        }
    }

    private fun getQuestionsList(): QuestionListResponse? {
        if (questionsList == null) {
            questionsList = jsonParser.parse(getJsonString(), QuestionListResponse::class.java)
        }

        return questionsList
    }

    private fun getJsonString() = assetFileLoader.loadFileAsStream("personality_test.json")
}
