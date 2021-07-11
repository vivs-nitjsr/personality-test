package com.personality.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal fun FragmentActivity.bindFragment(containerLayout: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .add(containerLayout, fragment, fragment.javaClass.name)
        .commit()
}

internal fun Disposable.disposedBy(compositeDisposable: CompositeDisposable): CompositeDisposable {
    compositeDisposable.add(this)

    return compositeDisposable
}