package ru.hse.routemoodclient.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.components.ButtonWithTitle
import ru.hse.routemoodclient.ui.components.GreenButton

@Composable
fun RouteSettings(
    routeViewModel: RouteViewModel,
    setRouteStart: () -> Unit,
    setRouteEnd: () -> Unit,
    onGenerateButtonClicked: () -> Unit,
    onDiscardButtonClicked: () -> Unit
    ) {
    val routeState by routeViewModel.routeState.collectAsState()
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "How would you like to walk today?",
                modifier = Modifier.padding(15.dp),
                fontSize = 25.sp
            )
        }
        item {
            OutlinedTextField(
                value = routeState.name,
                onValueChange = { routeViewModel.setName(it) },
                label = { Text("Route name") }
            )
        }
        item {
            ButtonWithTitle(
                title = "Route's start",
                onClick = setRouteStart,
                buttonText = "Set on map"
            )
        }
        item {
            ButtonWithTitle(
                title = "Route's end",
                onClick = setRouteEnd,
                buttonText = "Set on map"
            )
        }
        item {
            SetRouteRequest(routeViewModel)
        }
        item {
            Row {
                Spacer(Modifier.weight(0.25f, true))
                GreenButton(
                    onClick = onGenerateButtonClicked,
                    buttonText = "Generate the route"
                )
                Spacer(Modifier.weight(0.25f, true))
                GreenButton(
                    onClick = onDiscardButtonClicked,
                    buttonText = "Discard settings"
                )
                Spacer(Modifier.weight(0.25f, true))
            }
        }
    }
}

@Composable
fun SetRouteRequest(
    viewModel: RouteViewModel
) {
    val routeState by viewModel.routeState.collectAsState()
    OutlinedTextField(
        value = routeState.routeRequest,
        onValueChange = {viewModel.setRequest(it)},
        label = { Text("How would you like to walk today?") },
        modifier = Modifier.padding(15.dp)
    )
}

@Preview (showBackground = true)
@Composable
fun RouteSettingsPreview() {
    RouteSettings(
        routeViewModel = hiltViewModel(),
        setRouteStart = {},
        setRouteEnd = {},
        onGenerateButtonClicked = {},
        onDiscardButtonClicked = {}
    )
}