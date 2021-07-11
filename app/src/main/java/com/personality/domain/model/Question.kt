package com.personality.domain.model

internal data class Question(
    val question: String,
    val category: String,
    val questionType: QuestionType
)

internal data class QuestionType(
    val type: String,
    val options: List<String>? = null,
    val range: Range? = null,
    val condition: Condition? = null
)

internal data class Range(
    val from: String,
    val to: String
)

internal data class Condition(
    val predicate: Predicate,
    val ifPositive: Question
)

internal data class Predicate(
    val exactEquals: List<String>
)
