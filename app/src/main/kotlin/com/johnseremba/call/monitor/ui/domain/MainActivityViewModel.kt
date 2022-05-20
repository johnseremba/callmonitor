package com.johnseremba.call.monitor.ui.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnseremba.call.monitor.ui.domain.CallLogsUiContract.Event
import com.johnseremba.call.monitor.ui.domain.CallLogsUiContract.State
import com.johnseremba.call.monitor.ui.domain.CallLogsUiContract.State.Error
import com.johnseremba.call.monitor.ui.domain.CallLogsUiContract.State.Loading
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel(private val fetchLogsUseCase: FetchLogsUseCase) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    val uiState: StateFlow<State> = _state

    private val _event = MutableSharedFlow<Event>()
    private val viewEvent: SharedFlow<Event> = _event

    init {
        collectEvents()
    }

    private fun collectEvents() = viewModelScope.launch {
        viewEvent.collect {
            when (it) {
                is Event.FetchCallLogs -> fetchCallLogs()
            }
        }
    }

    private fun fetchCallLogs() {
        setState(Loading)
        viewModelScope.launch {
            try {
                val reports = fetchLogsUseCase()
                Log.d(TAG, "Call LogsList: $reports")
                setState(State.Success(reports))
            } catch (exception: Exception) {
                setState(Error(exception.message))
            }
        }
    }

    private fun setState(state: State) = viewModelScope.launch {
        _state.emit(state)
    }

    fun sendEvent(event: Event) = viewModelScope.launch {
        _event.emit(event)
    }
}
