package ru.hse.routemoodclient.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.data.RouteEntity
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.components.GreenButton

@Composable
fun RoutesListScreen(
    navigateUp : () -> Unit,
    routeViewModel: RouteViewModel,
    onSettingsClicked: () -> Unit
) {
    val routesList by routeViewModel.routesList.collectAsState()
    val routeUiState by routeViewModel.routeState.collectAsState()
    Scaffold {
        LazyColumn(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(modifier = Modifier.fillParentMaxWidth()) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            }
            item {
                Text(
                    text = "Current route",
                    fontSize = 20.sp
                )
            }
            item {
                SaveRouteEntity(
                    routeUiState = routeUiState,
                    onSaveClicked = {routeViewModel.saveRoute()},
                    onSettingsClicked = onSettingsClicked
                )
            }
            item {
                Text(
                    text = "Saved routes",
                    fontSize = 20.sp
                )
            }
            if (routesList.isNotEmpty()) {
                itemsIndexed(routesList) { index, routeEntity ->
                    ShowRouteEntity(
                        index = index,
                        routeEntity = routeEntity,
                        onDeleteClicked = {routeViewModel.deleteRoute(routeEntity.id)},
                        onSetClicked = {routeViewModel.setUiRouteById(routeEntity.id)}
                    )
                }
            } else {
                item {
                    Text(
                        text = "No saved routes",
                        fontSize = 20.sp
                    )
                }
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
}

@Composable
fun ShowRouteEntity(
    index: Int,
    routeEntity: RouteEntity,
    onDeleteClicked : () -> Unit,
    onSetClicked : () -> Unit
) {
    Card (
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Gray,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(Modifier.width(10.dp))
            Column (modifier = Modifier.weight(5f)) {
                Text(
                    text = "${routeEntity.name}:",
                    fontSize = 18.sp
                )
                Text(text = routeEntity.routeRequest,
                    fontSize = 20.sp)
            }
            IconButton(
                onClick = onSetClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.set_route_ui_icon),
                    contentDescription = "set icon",
                    modifier = Modifier.weight(1f)
                )
            }
            IconButton(
                onClick = onDeleteClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_icon),
                    contentDescription = "delete icon",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SaveRouteEntity(
    routeUiState: RouteUiState,
    onSaveClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    Card (
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Gray,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(Modifier.width(10.dp))
            Column (modifier = Modifier.weight(1f)) {
                Text(
                    text = "${routeUiState.name}:",
                    fontSize = 18.sp
                )
                Text(text = routeUiState.routeRequest,
                    fontSize = 20.sp)
            }
            IconButton(
                onClick = onSettingsClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.settings_icon),
                    contentDescription = "settings icon",
                    modifier = Modifier.weight(1f).requiredSize(30.dp)
                )
            }
            IconButton(
                onClick = onSaveClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save_icon),
                    contentDescription = "delete icon",
                    modifier = Modifier.weight(1f).requiredSize(30.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ShowRouteEntityPreview() {
    ShowRouteEntity(0, RouteEntity(), {}, {})
}

@Preview
@Composable
fun SaveRouteEntityPreview() {
    SaveRouteEntity(RouteUiState(), {}, {})
}