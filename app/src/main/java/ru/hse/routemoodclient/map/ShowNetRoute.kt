package ru.hse.routemoodclient.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ShowNetRoute (
    route: List<LatLng>
) {
    if (route.isEmpty()) {
        Text(
            text = "Route is empty!",
            modifier = Modifier.fillMaxSize(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    } else {
        val startMarkerState = rememberUpdatedMarkerState(route.first())
        val endMarkerState = rememberUpdatedMarkerState(route.last())

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(route.first(), 10f)
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
            uiSettings = mapUiSettings
        ) {
            Marker(
                state = startMarkerState,
                title = "Start"
            )

            Marker(
                state = endMarkerState,
                title = "End"
            )

            Polyline(
                points = route,
                color = Color.Blue,
                width = 10f
            )
        }
    }
}


@Preview
@Composable
fun ShowNetRoutePreview() {
    ShowNetRoute(
        route = listOf()
    )
}