package com.personality.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class QuestionResponse(
    @SerializedName("question")
    val question: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("question_type")
    val questionType: QuestionTypeResponse
)

internal data class QuestionTypeResponse(
    @SerializedName("type")
    val type: String,
    @SerializedName("options")
    val options: List<String>? = null,
    @SerializedName("range")
    val range: RangeResponse? = null,
    @SerializedName("condition")
    val condition: ConditionResponse? = null
)

internal data class RangeResponse(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String
)

internal data class ConditionResponse(
    @SerializedName("predicate")
    val predicate: PredicateResponse,
    @SerializedName("if_positive")
    val ifPositive: QuestionResponse
)

internal data class PredicateResponse(
    @SerializedName("exactEquals")
    val exactEquals: List<String>
)
