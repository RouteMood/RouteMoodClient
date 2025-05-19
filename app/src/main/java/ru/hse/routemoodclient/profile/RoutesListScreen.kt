package ru.hse.routemoodclient.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.data.RouteEntity
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.components.GreenButton
import ru.hse.routemoodclient.ui.theme.LightGreen

@Composable
fun RoutesListScreen(
    routeViewModel: RouteViewModel,
    onSettingsClicked: () -> Unit
) {
    val routesList by routeViewModel.routesList.collectAsState()
    val routeUiState by routeViewModel.routeState.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            Text(
                text = "Current route",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            SaveRouteEntity(
                routeUiState = routeUiState,
                onNameChange = { name -> routeViewModel.setName(name) },
                onDescriptionChange = { description -> routeViewModel.setRequest(description) },
                onSaveClicked = { routeViewModel.saveRoute() },
                onSettingsClicked = onSettingsClicked,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
        item {
            Text(
                text = "Saved routes",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        if (routesList.isNotEmpty()) {
            itemsIndexed(routesList) { index, routeEntity ->
                ShowRouteEntity(
                    index = index,
                    routeEntity = routeEntity,
                    onDeleteClicked = { routeViewModel.deleteRoute(routeEntity.id) },
                    onSetClicked = { routeViewModel.setUiRouteById(routeEntity.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        } else {
            item {
                Text(
                    text = "No saved routes",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            GreenButton(
                onClick = {
                    routeViewModel.deleteAllRoutes()
                },
                buttonText = "Delete all"
            )
        }
    }

}

@Composable
fun ShowRouteEntity(
    index: Int,
    routeEntity: RouteEntity,
    onDeleteClicked: () -> Unit,
    onSetClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = routeEntity.name,
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    onClick = onSetClicked,
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.set_route_ui_icon),
                        contentDescription = "Set route",
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClicked,
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = routeEntity.routeRequest,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )


        }
    }
}

@Composable
fun SaveRouteEntity(
    routeUiState: RouteUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = routeUiState.name,
                onValueChange = { onNameChange(it) },
                label = { Text("Route name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = routeUiState.routeRequest,
                onValueChange = { onDescriptionChange(it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onSettingsClicked,
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.settings_icon),
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onSaveClicked,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGreen,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save_icon),
                        contentDescription = "Save",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ShowRouteEntityPreview() {
    ShowRouteEntity(0, RouteEntity(routeRequest = "1\n2\n3\n4\n"), {}, {})
}

@Preview
@Composable
private fun SaveRouteEntityPreview() {
    SaveRouteEntity(RouteUiState(), {}, {}, {}, {})
}