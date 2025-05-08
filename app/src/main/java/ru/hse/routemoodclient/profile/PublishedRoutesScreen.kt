package ru.hse.routemoodclient.profile

import androidx.compose.foundation.Image
import ru.hse.routemoodclient.ui.PublishedRoute
import ru.hse.routemoodclient.ui.ServerViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Star
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
import ru.hse.routemoodclient.ui.theme.LightGreen

@Composable
fun PublishedRoutesScreen(
    routeViewModel: RouteViewModel,
    serverViewModel: ServerViewModel,
    onSettingsClicked: () -> Unit
) {
    val publishedRoutesList by serverViewModel.publishedRoutesState.collectAsState()
    val routeUiState by routeViewModel.routeState.collectAsState()
    Scaffold {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Current route",
                    fontSize = 20.sp
                )
            }
            item {
                PublishRouteEntity (
                    routeUiState = routeUiState,
                    onPublishClicked = {
                        serverViewModel.askSaveRoute()
                    },
                    onSettingsClicked = onSettingsClicked
                )
            }
            item {
                Row {
                    Text(
                        text = "Published routes",
                        fontSize = 20.sp
                    )
                    IconButton(
                        onClick = {
                            serverViewModel.askUserRoutes()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.refresh_icon),
                            contentDescription = "refresh icon",
                            modifier = Modifier
                                .size(35.dp)
                                .weight(1f)
                        )
                    }
                }
            }
            if (publishedRoutesList.isNotEmpty()) {
                itemsIndexed(publishedRoutesList) { index, publishedRoute ->
                    ShowPublishedRouteEntity(
                        index = index,
                        routeEntity = publishedRoute
                    )
                }
            } else {
                item {
                    Text(
                        text = "No published routes",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ShowPublishedRouteEntity(
    index: Int,
    routeEntity: PublishedRoute
) {
    Card (
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = LightGreen,
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
                    text = "${routeEntity.authorUsername}:",
                    fontSize = 18.sp
                )
                Text(
                    text = routeEntity.description,
                    fontSize = 20.sp
                )
            }
            Row {
                Text(
                    text = "${routeEntity.rating}",
                    fontSize = 20.sp
                )
                Image(
                    imageVector = Icons.Default.Star,
                    contentDescription = "star"
                )
            }

        }
    }
}

@Composable
fun PublishRouteEntity(
    routeUiState: RouteUiState,
    onPublishClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    Card (
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = LightGreen,
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
                    modifier = Modifier
                        .weight(1f)
                        .requiredSize(30.dp)
                )
            }
            IconButton(
                onClick = onPublishClicked
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = "delete icon",
                    modifier = Modifier
                        .weight(1f)
                        .requiredSize(30.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShowPublishedRouteEntityPreview() {
    ShowPublishedRouteEntity(0, PublishedRoute())
}

@Preview
@Composable
private fun PublishRouteEntityPreview() {
    PublishRouteEntity(RouteUiState(), {}, {})
}