package com.kbline.kotlin_module

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class KbViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    fun add(dis : Disposable) {
        disposable.addAll(dis)
    }

    override fun onCleared() {

        disposable.clear()
        super.onCleared()

    }
}