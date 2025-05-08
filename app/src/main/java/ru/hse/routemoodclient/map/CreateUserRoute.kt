package ru.hse.routemoodclient.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import ru.hse.routemoodclient.drawRoute.ApiService
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.components.GreenButton


@Composable
fun CreateUserRoute (
    routeViewModel: RouteViewModel,
    mapsApiKey: String
) {
    var markers by remember { mutableStateOf(listOf<MarkerState>()) }

    val petersburg = LatLng(59.9386, 30.3141)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(petersburg, 10f)
    }

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isBuildingEnabled = true,
                isIndoorEnabled = true
            )
        )
    }

    var mapUiSettings by remember { mutableStateOf(MapUiSettings()) }

    LocationPermission(
        { mapProperties = mapProperties.copy(isMyLocationEnabled = true) },
        { mapUiSettings = mapUiSettings.copy(myLocationButtonEnabled = true) }
    )

    val apiService: ApiService = viewModel()
    val builtPositions by apiService.positionsState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMapClick = { latLng ->
                val newMarker = MarkerState(position = latLng)
                markers = markers + newMarker
            }
        ) {
            for (markerState in markers) {
                Marker(
                    state = markerState,
                    onClick = { marker ->
                        markers = markers.filter { it.position != marker.position }
                        true
                    }
                )
            }
            if (markers.isNotEmpty()) {
                Polyline(
                    points = markers.map { markerState -> markerState.position },
                    width = 10f
                )

            }
            Polyline(
                points = builtPositions,
                width = 10f
            )
        }
        Column {
            GreenButton(
                onClick = {
                    if (markers.size > 1) {
                        apiService.updatePositions(markers.map { it.position })
                    }
                },
                buttonText = "Build"
            )
            GreenButton(
                onClick = {
                    apiService.clearRoute()
                    markers = listOf<MarkerState>()
                },
                buttonText = "Clear"
            )
            GreenButton(
                onClick = {
                    if (builtPositions.isEmpty()) {
                        routeViewModel.setRoute(markers.map { markerState -> markerState.position })
                    } else {
                        routeViewModel.setRoute(builtPositions)
                    }
                },
                buttonText = "Save"
            )
        }
    }

}
