package com.personality.domain.repo

import com.personality.data.remote.model.QuestionResponse
import io.reactivex.Observable

internal interface RemoteRepository {
    fun getCategories(): Observable<List<String>>
    fun getQuestions(category: String): Observable<List<QuestionResponse>>
}