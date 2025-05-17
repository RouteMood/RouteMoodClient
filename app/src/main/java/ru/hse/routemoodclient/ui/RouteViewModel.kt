package ru.hse.routemoodclient.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.hse.routemoodclient.data.DataRepository
import ru.hse.routemoodclient.data.RouteEntity
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.data.toRouteEntity
import ru.hse.routemoodclient.data.toRouteUiState
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
    val routesList: StateFlow<List<RouteEntity>> = dataRepository.getRouteList()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveRoute() {
        val routeEntity: RouteEntity = routeState.value.toRouteEntity()
        viewModelScope.launch {
            dataRepository.insertRoute(routeEntity)
        }
    }

    fun setUiRouteById (index : Int) {
        viewModelScope.launch {
            val routeEntity: RouteEntity = dataRepository.getRouteById(index).first()
            dataRepository.updateRouteState(routeEntity.toRouteUiState())
        }
    }

    fun deleteRoute(index : Int) {
        viewModelScope.launch {
            val routeEntity: RouteEntity = dataRepository.getRouteById(index).first()
            dataRepository.deleteRoute(routeEntity)
        }
    }

    fun deleteAllRoutes() {
        viewModelScope.launch {
            dataRepository.nukeTable()
        }
    }

    /**
     * Set the route's name.
     */
    fun setName(name: String) {
        val updatedRouteState = routeState.value.copy(
            name = name
        )
        dataRepository.updateRouteState(updatedRouteState)
    }

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
     * Set the whole coordinates for this route's state.
     */
    fun setRoute(coordinates : List<LatLng>) {
        val updatedRouteState = routeState.value.copy(
            start = coordinates.first(),
            isStartSet = true,
            end = coordinates.last(),
            isEndSet = true,
            route = coordinates
        )
        dataRepository.updateRouteState(updatedRouteState)
    }

    fun getServerRoute(routeEntity: PublishedRoute) {
        val updatedRouteState : RouteUiState
        if (routeEntity.route.isEmpty()) {
            updatedRouteState = routeState.value.copy(
                name = routeEntity.name,
                routeRequest = routeEntity.description,
                route = routeEntity.route
            )
        } else {
            updatedRouteState = routeState.value.copy(
                start = routeEntity.route.first(),
                isStartSet = true,
                end = routeEntity.route.last(),
                isEndSet = true,
                name = routeEntity.name,
                routeRequest = routeEntity.description,
                route = routeEntity.route
            )
        }

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