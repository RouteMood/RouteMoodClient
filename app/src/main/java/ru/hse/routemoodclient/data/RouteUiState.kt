package ru.hse.routemoodclient.data

import com.google.android.gms.maps.model.LatLng

/**
 * Data class that represents the current UI state in terms of [start], [end], [routeRequest]
 */
data class RouteUiState(
    val start : LatLng = LatLng(59.9386, 30.3141),
    val isStartSet : Boolean = false,
    val end : LatLng = LatLng(59.9386, 30.3141),
    val isEndSet : Boolean = false,
    val routeRequest : String = "Default route",
    val route: List<LatLng> = listOf()
)