package ru.hse.routemoodclient.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import ru.hse.routemoodclient.screens.LoginScreen
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.ServerViewModel

@Composable
fun rememberUpdatedMarkerState(newPosition: LatLng): MarkerState =
    remember { MarkerState(position = newPosition) }
        .apply { position = newPosition }

@Composable
fun ShowMap (
    viewModel: RouteViewModel,
    onMapClick: (LatLng) -> Unit
) {
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
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
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
                points =  routeState.route
            )
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