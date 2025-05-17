package ru.hse.routemoodclient.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import ru.hse.routemoodclient.ui.PublishedRoute
import ru.hse.routemoodclient.ui.ServerViewModel
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.data.RouteEntity
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.components.GreenButton
import ru.hse.routemoodclient.ui.theme.LightGreen
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishedRoutesScreen(
    routeViewModel: RouteViewModel,
    serverViewModel: ServerViewModel,
    onSettingsClicked: () -> Unit
) {
    val publishedRoutesList by serverViewModel.publishedRoutesState.collectAsState()
    val routeUiState by routeViewModel.routeState.collectAsState()

    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        state = state,
        isRefreshing = serverViewModel.isRefreshing,
        onRefresh = {
            serverViewModel.askUserRoutes()
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                PublishRouteEntity(
                    routeUiState = routeUiState,
                    onPublishClicked = {
                        serverViewModel.askSaveRoute()
                    },
                    onSettingsClicked = onSettingsClicked,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
            item {
                Text(
                    text = "Published routes",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            if (publishedRoutesList.isNotEmpty()) {
                itemsIndexed(publishedRoutesList) { index, publishedRoute ->
                    ShowPublishedRouteEntity(
                        index = index,
                        routeEntity = publishedRoute,
                        onSaveClicked = { routeEntity ->
                            routeViewModel.getServerRoute(routeEntity)
                        },
                        onDeleteClicked = { uuid ->
                            serverViewModel.askDeleteRoute(uuid)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                item {
                    Text(
                        text = "No published routes",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun ShowPublishedRouteEntity(
    index: Int,
    routeEntity: PublishedRoute,
    onSaveClicked: (routeEntity: PublishedRoute) -> Unit,
    onDeleteClicked: (UUID) -> Unit,
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = routeEntity.authorUsername,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = routeEntity.description,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = routeEntity.rating.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "star",
                    tint = Color.Yellow
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { onSaveClicked(routeEntity) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save_icon),
                    contentDescription = "delete icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { onDeleteClicked(routeEntity.id) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_icon),
                    contentDescription = "delete icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun PublishRouteEntity(
    routeUiState: RouteUiState,
    onPublishClicked: () -> Unit,
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = routeUiState.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = routeUiState.routeRequest,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
            IconButton(
                onClick = onSettingsClicked,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.settings_icon),
                    contentDescription = "settings icon"
                )
            }
            Spacer(Modifier.width(10.dp))
            IconButton(
                onClick = onPublishClicked,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = "send icon",
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShowPublishedRouteEntityPreview() {
    ShowPublishedRouteEntity(0, PublishedRoute(), {}, {})
}

@Preview
@Composable
private fun PublishRouteEntityPreview() {
    PublishRouteEntity(RouteUiState(), {}, {})
}