package com.johnseremba.call.monitor.ui.domain

import com.johnseremba.call.monitor.server.data.CallEntry

class CallLogsUiContract {
    sealed class Event {
        object FetchCallLogs : Event()
    }

    sealed class State(val isLoading: Boolean) {
        object Loading : State(true)
        data class Success(val data: List<CallEntry>) : State(false)
        data class Error(val message: String?) : State(false)
    }
}
