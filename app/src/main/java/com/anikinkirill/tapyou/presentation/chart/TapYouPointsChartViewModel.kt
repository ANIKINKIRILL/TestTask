package com.anikinkirill.tapyou.presentation.chart

import androidx.lifecycle.viewModelScope
import com.anikinkirill.tapyou.domain.Resource.Companion.onError
import com.anikinkirill.tapyou.domain.Resource.Companion.onSuccess
import com.anikinkirill.tapyou.domain.Text
import com.anikinkirill.tapyou.domain.model.TapYouPoints
import com.anikinkirill.tapyou.domain.usecase.GetTapYouPointsUseCase
import com.anikinkirill.tapyou.presentation.chart.TapYouPointsChartViewModel.Action
import com.anikinkirill.tapyou.presentation.chart.TapYouPointsChartViewModel.State
import com.anikinkirill.tapyou.presentation.utils.BaseViewModel
import kotlinx.coroutines.launch

class TapYouPointsChartViewModel(
    private val useCase: GetTapYouPointsUseCase,
) : BaseViewModel<State, Action>(State()) {

    fun loadPoints(amount: Int) {
        updateState {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            useCase.execute(amount)
                .onSuccess { result ->
                    updateState {
                        it.copy(
                            points = result,
                            isLoading = false,
                            errorState = null,
                        )
                    }
                }
                .onError { error ->
                    updateState {
                        it.copy(
                            points = emptyList(),
                            isLoading = false,
                            errorState = ErrorState(error)
                        )
                    }
                }
        }
    }

    data class State(
        val points: List<TapYouPoints> = emptyList(),
        val isLoading: Boolean = false,
        val errorState: ErrorState? = null,
    )

    data class ErrorState(
        val errorText: Text,
    )

    sealed class Action
}