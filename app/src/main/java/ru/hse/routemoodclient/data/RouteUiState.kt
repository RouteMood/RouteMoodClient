package ru.hse.routemoodclient.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

/**
 * Entity data class that represents the current UI state in terms of [start], [end], [routeRequest], [route]
 */
data class RouteUiState(
    val start : LatLng = LatLng(59.9386, 30.3141),
    val isStartSet : Boolean = false,
    val end : LatLng = LatLng(59.9386, 30.3141),
    val isEndSet : Boolean = false,
    val name : String = "My route",
    val routeRequest : String = "Default route",
    val route: List<LatLng> = listOf()
)

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "My route ${id+1}",
    val routeRequest : String = "Default route",
    val route: List<LatLng> = listOf()
)

/**
 * Extension function to convert [RouteUiState] to [RouteEntity].
 */
fun RouteUiState.toRouteEntity(): RouteEntity {
    return RouteEntity(
        name = this.name,
        routeRequest = this.routeRequest,
        route = this.route
    )
}

/**
 * Extension function to convert [RouteEntity] to [RouteUiState]
 */
fun RouteEntity.toRouteUiState(isEntryValid: Boolean = false): RouteUiState {
    if (route.isEmpty()) {
        return RouteUiState(
            isStartSet = true,
            isEndSet = true,
            name = this.name,
            routeRequest = this.routeRequest,
            route = this.route
        )
    } else {
        return RouteUiState(
            start = this.route[0],
            isStartSet = true,
            end = this.route.last(),
            isEndSet = true,
            name = this.name,
            routeRequest = this.routeRequest,
            route = this.route
        )
    }

}