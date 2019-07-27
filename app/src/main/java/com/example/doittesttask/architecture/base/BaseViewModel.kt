package com.example.doittesttask.architecture.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel(), LifecycleObserver, CoroutineScope by MainScope() {

    private var viewLifecycle: Lifecycle? = null

    fun attachView(viewLifecycle: Lifecycle) {
        this.viewLifecycle = viewLifecycle

        viewLifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onViewDestroyed() {
        viewLifecycle = null
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}