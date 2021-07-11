package com.personality.domain.usecase

import com.personality.core.RxUseCase
import com.personality.domain.repo.RemoteRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class GetCategoriesUseCase @Inject constructor(
    private val repo: RemoteRepository
) : RxUseCase<Unit, List<String>> {

    override fun run(param: Unit?): Observable<List<String>> {
        return repo.getCategories()
            .subscribeOn(Schedulers.io())
    }
}
