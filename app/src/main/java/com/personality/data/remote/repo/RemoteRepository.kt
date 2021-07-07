package com.personality.data.remote.repo

import com.personality.core.JsonParser
import io.reactivex.Observable

internal interface RemoteRepository {
    fun getCategories(): Observable<List<String>>
}

internal class RemoteRepositoryImpl constructor(
    private val jsonParser: JsonParser
) : RemoteRepository {

    override fun getCategories(): Observable<List<String>> {
        TODO("Not yet implemented")
    }
}
