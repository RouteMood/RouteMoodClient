package ru.hse.routemoodclient.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val routeDao: RouteDao) {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _routeState = MutableStateFlow(RouteUiState())
    val routeState: StateFlow<RouteUiState> = _routeState.asStateFlow()

    fun getRouteList() : Flow<List<RouteEntity>> {
        return routeDao.getAllRoutes()
    }

    suspend fun insertRoute(route: RouteEntity) {
        routeDao.insert(route)
    }

    fun getRouteById(id: Int): Flow<RouteEntity> {
        return routeDao.getRouteById(id)
    }

    suspend fun deleteRoute(route: RouteEntity) {
        routeDao.delete(route)
    }

    suspend fun nukeTable() {
        routeDao.nukeTable()
        routeDao.resetAutoIncrement()
    }

    fun updateUserState(newUserState: UserState) {
        _userState.update {
            newUserState
        }
    }

    fun updateRouteState(newRouteState: RouteUiState) {
        _routeState.update {
            newRouteState
        }
    }
}