package ru.hse.routemoodclient.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import ru.hse.routemoodclient.screens.LoginScreen
import ru.hse.routemoodclient.ui.RouteViewModel

@Composable
fun rememberUpdatedMarkerState(newPosition: LatLng): MarkerState =
    remember { MarkerState(position = newPosition) }
        .apply { position = newPosition }

@Composable
fun ShowMap (
    viewModel: RouteViewModel,
    onMapClick: (LatLng) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val startMarkerState = rememberUpdatedMarkerState(uiState.start)
    val endMarkerState = rememberUpdatedMarkerState(uiState.end)
    val petersburg = LatLng(59.9386, 30.3141)
    val cameraPositionState = rememberCameraPositionState {
        position = if (uiState.isStartSet) {
            CameraPosition.fromLatLngZoom(uiState.start, 15f)
        } else {
            CameraPosition.fromLatLngZoom(petersburg, 15f)
        }

    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = onMapClick
    ) {
        if (uiState.isStartSet) {
            Marker(
                state = startMarkerState,
                title = "Start"
            )
        }
        if (uiState.isEndSet) {
            Marker(
                state = endMarkerState,
                title = "End"
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