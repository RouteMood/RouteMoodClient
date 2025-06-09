package ru.hse.routemoodclient.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.profile.ProfileImagePainter
import ru.hse.routemoodclient.ui.PublishedRoute
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.ServerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(
    routeViewModel: RouteViewModel,
    serverViewModel: ServerViewModel,
    toRoutePreview: () -> Unit,
) {
    val globalRoutes by serverViewModel.networkRoutesState.collectAsState()
    val userState by serverViewModel.userState.collectAsState()


    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        state = state,
        isRefreshing = serverViewModel.isRefreshing,
        onRefresh = {
            serverViewModel.askFirstPageRoutes()
            // serverViewModel.askListRoutes()
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
            itemsIndexed(globalRoutes) { _, route ->
                RouteCard(
                    routeEntity = route,
                    routeViewModel = routeViewModel,
                    serverViewModel = serverViewModel,
                    toRoutePreview = toRoutePreview,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                Button(
                    onClick = {
                        serverViewModel.askNextPageRoutes()
                    }
                ) {
                    Text("Load next")
                }
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
    routeEntity: PublishedRoute,
    routeViewModel: RouteViewModel,
    serverViewModel: ServerViewModel,
    toRoutePreview: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                serverViewModel.setPreviewRoute(routeEntity.route)
                toRoutePreview()
            }
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
                        .size(60.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileImagePainter(
                        routeEntity.profileId,
                        serverViewModel
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
                userRate = routeEntity.userRating,
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
    userRate: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        for (i in 1..maxRating) {
            val starColor by animateColorAsState(
                when {
                    i <= userRate -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                },
                label = "starColor"
            )

            IconButton(
                onClick = {
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

