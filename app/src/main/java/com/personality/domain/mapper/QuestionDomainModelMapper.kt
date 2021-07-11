package com.personality.domain.mapper

import com.personality.core.Mapper
import com.personality.data.remote.model.QuestionResponse
import com.personality.domain.model.*
import javax.inject.Inject

internal class QuestionDomainModelMapper @Inject constructor() :
    Mapper<QuestionResponse, Question> {

    override fun map(from: QuestionResponse): Question {
        return Question(
            question = from.question,
            category = from.category,
            questionType = with(from.questionType) {
                QuestionType(
                    type = type,
                    options = options,
                    range = range?.let { Range(from = it.from, to = it.to) },
                    condition = condition?.let {
                        Condition(
                            predicate = Predicate(it.predicate.exactEquals),
                            ifPositive = map(it.ifPositive)
                        )
                    }
                )
            }
        )
    }
}
