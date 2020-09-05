package com.astutify.mvi

import io.reactivex.disposables.Disposable

internal class DisposableCollection : Disposable {

    private var disposables: MutableList<Disposable>? = mutableListOf()

    override fun isDisposed(): Boolean =
        disposables == null

    override fun dispose() {
        disposables?.forEach { it.dispose() }
        disposables = null
    }

    fun add(obj: Any?) {
        if (obj is Disposable) {
            disposables?.apply {
                removeAll { it.isDisposed }
                add(obj)
            }
        }
    }
}
