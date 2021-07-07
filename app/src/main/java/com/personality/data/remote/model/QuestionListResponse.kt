package com.personality.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class QuestionListResponse(
    @SerializedName("categories")
    val categories: List<String>,
    @SerializedName("questions")
    val questions: List<QuestionResponse>
)