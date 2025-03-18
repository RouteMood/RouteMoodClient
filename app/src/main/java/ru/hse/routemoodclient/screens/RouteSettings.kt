package ru.hse.routemoodclient.screens

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hse.routemoodclient.ui.RouteViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.hse.routemoodclient.data.RouteUiState

@Composable
fun RouteSettings(
    viewModel: RouteViewModel,
    setRouteStart: () -> Unit,
    setRouteEnd: () -> Unit,
    onGenerateButtonClicked: () -> Unit,
    onDiscardButtonClicked: () -> Unit
    ) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How would you like to walk today?",
            modifier = Modifier.padding(15.dp),
            fontSize = 25.sp
        )
        ButtonWithTitle(
            title = "Route's start",
            onClick = setRouteStart,
            buttonText = "Set on map"
        )
        ButtonWithTitle(
            title = "Route's end",
            onClick = setRouteEnd,
            buttonText = "Set on map"
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Time",
                modifier = Modifier.padding(10.dp),
                fontSize = 20.sp
            )
            RouteTime(viewModel)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Distance",
                modifier = Modifier.padding(10.dp),
                fontSize = 20.sp
            )
            RouteLength(viewModel)
        }
        Row {
            Spacer(Modifier.weight(0.25f, true))
            Button(onClick = onGenerateButtonClicked) {
                Text("Generate the route")
            }
            Spacer(Modifier.weight(0.25f, true))
            Button(onClick = onDiscardButtonClicked) {
                Text("Discard settings")
            }
            Spacer(Modifier.weight(0.25f, true))
        }

    }
}

@Composable
fun ButtonWithTitle (
    title: String,
    onClick: () -> Unit,
    buttonText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(10.dp),
            fontSize = 20.sp
        )
        Button(onClick = onClick) {
            Text(buttonText)
        }
    }
}

@Composable
fun RouteTime(
    viewModel: RouteViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val times = listOf(30, 60, 90)
    Row() {
        Spacer(Modifier.weight(0.25f, true))
        times.forEach { time ->
            Button(
                onClick = {
                    viewModel.setTime(time)
                },
                enabled = uiState.time != time
            ) {
                Text(text = "$time min")
            }
            Spacer(Modifier.weight(0.25f, true))
        }
    }
}

@Composable
fun RouteLength(
    viewModel: RouteViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val distances = listOf(1, 3, 5)
    Row() {
        Spacer(Modifier.weight(0.25f, true))
        distances.forEach { distance ->
            Button(
                onClick = {
                    viewModel.setLength(distance)
                },
                enabled = uiState.length != distance
            ) {
                Text(text = "$distance km")
            }
            Spacer(Modifier.weight(0.25f, true))
        }
    }
}

@Preview (showBackground = true)
@Composable
fun RouteSettingsPreview() {
    RouteSettings(
        viewModel = viewModel(),
        setRouteStart = {},
        setRouteEnd = {},
        onGenerateButtonClicked = {},
        onDiscardButtonClicked = {}
    )
}