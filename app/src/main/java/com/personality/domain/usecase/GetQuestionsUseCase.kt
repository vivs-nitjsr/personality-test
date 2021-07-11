package com.personality.domain.usecase

import com.personality.core.RxUseCase
import com.personality.domain.mapper.QuestionDomainModelMapper
import com.personality.domain.model.Question
import com.personality.domain.repo.RemoteRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class GetQuestionsUseCase @Inject constructor(
    private val repository: RemoteRepository,
    private val mapper: QuestionDomainModelMapper
) : RxUseCase<String, List<Question>> {

    override fun run(param: String?): Observable<List<Question>> {
        checkNotNull(param) { "category cannot be null" }

        return repository.getQuestions(category = param)
            .map { questionsList -> questionsList.map { mapper.map(it) } }
            .subscribeOn(Schedulers.io())
    }
}
