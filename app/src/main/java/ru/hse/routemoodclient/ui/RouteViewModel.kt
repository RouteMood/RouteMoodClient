package ru.hse.routemoodclient.ui

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.hse.routemoodclient.data.DataRepository
import ru.hse.routemoodclient.data.RouteUiState
import javax.inject.Inject

/**
 * [RouteViewModel] holds information about a route settings in terms of length, time.
 */
@HiltViewModel
class RouteViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    /**
     * Route state
     */
    val routeState: StateFlow<RouteUiState> = dataRepository.routeState

    /**
     * Set the start [latitude] and [longitude] for this route's state.
     */
    fun setStart(latitude: Double,
                 longitude: Double) {
        val updatedRouteState = routeState.value.copy(
            start = LatLng(latitude, longitude),
            isStartSet = true
        )
        dataRepository.updateRouteState(updatedRouteState)
    }

    /**
     * Set the end [latitude] and [longitude] for this route's state.
     */
    fun setEnd(latitude: Double,
                 longitude: Double) {
        val updatedRouteState = routeState.value.copy(
            end = LatLng(latitude, longitude),
            isEndSet = true
        )
        dataRepository.updateRouteState(updatedRouteState)
    }

    /**
     * Set the [routeRequest] for this route's state.
     */
    fun setRequest(routeRequest: String) {
        val updatedRouteState = routeState.value.copy(
            routeRequest = routeRequest
        )
        dataRepository.updateRouteState(updatedRouteState)
    }

    /**
     * Reset the route state
     */
    fun resetRoute() {
        val updatedRouteState = RouteUiState()
        dataRepository.updateRouteState(updatedRouteState)
    }
}