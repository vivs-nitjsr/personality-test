package com.personality.core

import io.reactivex.Observable

internal interface RxUseCase<Param, Result> {
    fun run(param: Param? = null): Observable<Result>
}
