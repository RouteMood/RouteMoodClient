package ru.hse.routemoodclient.data

import com.google.android.gms.maps.model.LatLng
/**
 * Data class that represents the current UI state in terms of [start], [end], [length], [time]
 */
data class RouteUiState(
    val start : LatLng = LatLng(59.9386, 30.3141),
    val isStartSet : Boolean = false,
    val end : LatLng = LatLng(59.9386, 30.3141),
    val isEndSet : Boolean = false,
    /** Selected route length (1, 3, 5) */
    val length: Int = 0,
    /** Selected route time (30, 60, 90) */
    val time: Int = 0
)