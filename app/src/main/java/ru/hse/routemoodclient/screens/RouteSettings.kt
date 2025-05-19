package ru.hse.routemoodclient.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.components.ButtonWithTitle
import ru.hse.routemoodclient.ui.components.GreenButton
import ru.hse.routemoodclient.ui.theme.LightGreen

@Composable
fun RouteSettings(
    routeViewModel: RouteViewModel,
    setRouteStart: () -> Unit,
    setRouteEnd: () -> Unit,
    setWholeRoute: () -> Unit,
    onGenerateButtonClicked: () -> Unit,
    onDiscardButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    val routeState by routeViewModel.routeState.collectAsState()
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "How would you like to walk today?",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 24.dp),
                fontSize = 25.sp
            )
        }
        item {
            OutlinedTextField(
                value = routeState.name,
                onValueChange = { routeViewModel.setName(it) },
                label = {
                    Text(
                        "Name",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
        item {
            OutlinedTextField(
                value = routeState.routeRequest,
                onValueChange = {routeViewModel.setRequest(it)},
                label = {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Generate the route",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    MapActionButton(
                        title = "Route's start",
                        icon = Icons.Default.Place,
                        onClick = setRouteStart,
                        modifier = Modifier.fillMaxWidth()
                    )

                    MapActionButton(
                        title = "Route's end",
                        icon = Icons.Default.Place,
                        onClick = setRouteEnd,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = onGenerateButtonClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Generate Route")
                    }
                }
            }
        }
        item {
            MapActionButton(
                title = "Make my own route",
                icon = Icons.Default.Edit,
                onClick = setWholeRoute,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = onDiscardButtonClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen,
                    contentColor = Color.Black
                )
            ) {
                Text("Discard Settings")
            }
        }
    }
}

@Composable
fun MapActionButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 1.dp,
            pressedElevation = 0.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1F))
        }
    }
}

@Preview (showBackground = true)
@Composable
fun RouteSettingsPreview() {
    RouteSettings(
        routeViewModel = hiltViewModel(),
        setRouteStart = {},
        setRouteEnd = {},
        setWholeRoute = {},
        onGenerateButtonClicked = {},
        onDiscardButtonClicked = {}
    )
}