package ru.hse.routemoodclient.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor() {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _routeState = MutableStateFlow(RouteUiState())
    val routeState: StateFlow<RouteUiState> = _routeState.asStateFlow()

    fun updateUserState(newUserState: UserState) {
        _userState.value = newUserState
    }

    fun updateRouteState(newRouteState: RouteUiState) {
        _routeState.value = newRouteState
    }
}