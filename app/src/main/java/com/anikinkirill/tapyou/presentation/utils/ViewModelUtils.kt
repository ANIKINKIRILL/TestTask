package com.anikinkirill.tapyou.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE : Any, SIDE_EFFECT : Any>(
    private val defaultStateHolder: StateHolder<STATE>
) : ViewModel(), StateFlow<STATE> by defaultStateHolder {

    constructor(initialState: STATE) : this(StateHolder(initialState))

    val default = Effect<SIDE_EFFECT>()

    protected fun updateState(updater: (STATE) -> STATE) {
        defaultStateHolder.updateState(updater)
    }

    protected fun postSideEffect(effect: SIDE_EFFECT) {
        default.postSideEffect(effect)
    }
}

class StateHolder<STATE : Any> private constructor(
    private val mutableStateFlow: MutableStateFlow<STATE>,
    private val stateFlow: StateFlow<STATE> = mutableStateFlow.asStateFlow(),
) : StateFlow<STATE> by stateFlow {
    constructor(
        initialState: STATE,
    ) : this(
        MutableStateFlow(initialState),
    )

    fun updateState(updater: (STATE) -> STATE) {
        mutableStateFlow.update(updater)
    }
}

class Effect<SIDE_EFFECT : Any> {
    private val sideEffectChannel = Channel<SIDE_EFFECT>()
    val effectFlow = sideEffectChannel.receiveAsFlow()

    context(ViewModel)
    fun postSideEffect(sideEffect: SIDE_EFFECT) {
        viewModelScope.launch {
            sideEffectChannel.send(sideEffect)
        }
    }
}