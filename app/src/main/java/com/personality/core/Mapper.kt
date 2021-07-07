package com.personality.core

internal interface Mapper<From, To> {
    fun map(from: From): To
}
