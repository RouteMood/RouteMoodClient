package ru.hse.routemoodclient.ui

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.hse.routemoodclient.data.RouteUiState

/**
 * [RouteViewModel] holds information about a route settings in terms of length, time.
 */
class RouteViewModel : ViewModel() {

    /**
     * Route state
     */
    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    /**
     * Set the start [latitude] and [longitude] for this route's state.
     */
    fun setStart(latitude: Double,
                 longitude: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                start = LatLng(latitude, longitude),
                isStartSet = true
            )
        }
    }

    /**
     * Set the end [latitude] and [longitude] for this route's state.
     */
    fun setEnd(latitude: Double,
                 longitude: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                end = LatLng(latitude, longitude),
                isEndSet = true
            )
        }
    }

    /**
     * Set the length [routeLength] for this route's state.
     */
    fun setLength(routeLength: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                length = routeLength
            )
        }
    }

    /**
     * Set the time [routeTime] for this route's state.
     */
    fun setTime(routeTime: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                time = routeTime
            )
        }
    }

    /**
     * Reset the route state
     */
    fun resetRoute() {
        _uiState.value = RouteUiState()
    }
}