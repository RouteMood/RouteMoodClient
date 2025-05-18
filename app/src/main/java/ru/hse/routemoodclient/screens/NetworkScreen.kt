package ru.hse.routemoodclient.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.data.RouteEntity
import ru.hse.routemoodclient.ui.PublishedRoute
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.theme.LightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(
    routeViewModel: RouteViewModel,
    serverViewModel: ServerViewModel
) {
    val globalRoutes by serverViewModel.networkRoutesState.collectAsState()

    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        state = state,
        isRefreshing = serverViewModel.isRefreshing,
        onRefresh = {
            serverViewModel.askListRoutes()
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                Text(
                    text = "Popular routes",
                    fontSize = 35.sp
                )
            }
            item {
                Spacer(modifier = Modifier.height(35.dp))
            }
            itemsIndexed(globalRoutes) { id, route ->
                RouteCard(
                    index = id,
                    routeEntity = route,
                    routeViewModel = routeViewModel,
                    serverViewModel = serverViewModel,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RouteCard(
    index: Int,
    routeEntity: PublishedRoute,
    routeViewModel: RouteViewModel,
    serverViewModel: ServerViewModel,
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
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = routeEntity.authorUsername,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { routeViewModel.getServerRoute(routeEntity) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save_icon),
                        contentDescription = "delete icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = routeEntity.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            RatingBar(
                globalRating = routeEntity.rating,
                onRatingChanged = { newRating ->
                    serverViewModel.askAddRate(
                        routeId = routeEntity.id,
                        rating = newRating
                    )
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = routeEntity.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun RatingBar(
    globalRating: Double,
    maxRating: Int = 5,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var userRating by remember { mutableIntStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        for (i in 1..maxRating) {
            val starColor by animateColorAsState(
                when {
                    i <= userRating -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                },
                label = "starColor"
            )

            IconButton(
                onClick = {
                    userRating = i
                    onRatingChanged(i)
                },
                modifier = Modifier.size(32.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = starColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rate $i stars",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$globalRating",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "/5",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/*
@Composable
fun RouteCard(
    index: Int,
    routeEntity: PublishedRoute,
    serverViewModel: ServerViewModel
) {
    Card(
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
            Column(modifier = Modifier.weight(5f)) {
                Text(
                    text = "${routeEntity.name}:",
                    fontSize = 18.sp
                )
                RatingBar(
                    globalRating = routeEntity.rating,
                    onRatingChanged = { newRating ->
                        serverViewModel.askAddRate(
                            routeId = routeEntity.id,
                            rating = newRating
                        )
                    }
                )
                Text(
                    text = routeEntity.description,
                    fontSize = 20.sp
                )
            }
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "save icon",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    globalRating: Double,
    maxRating: Int = 5,
    onRatingChanged: (Int) -> Unit
) {
    var userRating by remember { mutableIntStateOf(0) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxRating) {
            IconButton(
                onClick = {
                    onRatingChanged(i)
                    userRating = i
                },
                modifier = Modifier.size(25.dp)
            ) {
                if (i <= userRating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "filled star",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Yellow
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "filled star",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Gray
                    )
                }
            }
        }
        Text(
            text = "Total: ${globalRating}",
            fontSize = 20.sp
        )
    }
}
*/
