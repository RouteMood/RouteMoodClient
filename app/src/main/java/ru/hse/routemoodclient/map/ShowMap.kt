package ru.hse.routemoodclient.map

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import ru.hse.routemoodclient.screens.LoadingScreen
import ru.hse.routemoodclient.screens.LoginScreen
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.ServerViewModel

@Composable
fun rememberUpdatedMarkerState(newPosition: LatLng): MarkerState =
    remember { MarkerState(position = newPosition) }
        .apply { position = newPosition }

@Composable
fun ShowMap (
    viewModel: ServerViewModel,
    onMapClick: (LatLng) -> Unit
) {
//    if (viewModel.isLoading()) {
//        LoadingScreen(viewModel)
//        return
//    }

    val routeState by viewModel.routeState.collectAsState()

    val startMarkerState = rememberUpdatedMarkerState(routeState.start)
    val endMarkerState = rememberUpdatedMarkerState(routeState.end)

    val petersburg = LatLng(59.9386, 30.3141)
    val cameraPositionState = rememberCameraPositionState {
        position = if (routeState.isStartSet) {
            CameraPosition.fromLatLngZoom(routeState.start, 10f)
        } else {
            CameraPosition.fromLatLngZoom(petersburg, 10f)
        }

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
        {
            mapProperties = mapProperties.copy(
                isBuildingEnabled = true,
                isIndoorEnabled = true,
                isMyLocationEnabled = true
            )
        },
        {
            mapUiSettings = mapUiSettings.copy(
                indoorLevelPickerEnabled = true,
                mapToolbarEnabled = true,
                myLocationButtonEnabled = true,
                rotationGesturesEnabled = true
            )
        }
    )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = mapUiSettings,
        onMapClick = onMapClick
    ) {
        if (routeState.isStartSet) {
            Marker(
                state = startMarkerState,
                title = "Start"
            )
        }
        if (routeState.isEndSet) {
            Marker(
                state = endMarkerState,
                title = "End"
            )
        }
        if (routeState.route.isNotEmpty()) {
            Polyline(
                points =  routeState.route,
                width = 10f
            )
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(
    setMapProperties: () -> Unit,
    setMapUiSettings: () -> Unit
) {
    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }

    when (locationPermissionState.status) {
        is PermissionStatus.Granted -> {
            setMapProperties()
            setMapUiSettings()
        }
        is PermissionStatus.Denied -> {
            Column {
                Text("Location permission denied\n Sorry, we need this permission to show your location on the map")
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}

@Preview
@Composable
fun ShowMapPreview() {
    ShowMap(
        viewModel = viewModel(),
        onMapClick = {}
    )
}