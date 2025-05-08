package ru.hse.routemoodclient.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.theme.LightGreen

@Composable
fun NetworkScreen(
    serverViewModel: ServerViewModel
) {
    val globalRoutes by serverViewModel.networkRoutesState.collectAsState()
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                text = "Popular routes",
                modifier = Modifier.weight(3.0F),
                fontSize = 35.sp
            )
            IconButton(
                onClick = {
                    serverViewModel.askListRoutes()
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
        // public routes
        LazyColumn {
            itemsIndexed (globalRoutes) { id, route ->
                RouteCard(
                    index = id,
                    routeEntity = route,
                    serverViewModel = serverViewModel
                )
            }
        }
    }
}

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
    var userRating by remember { mutableIntStateOf(4) }
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

@Preview
@Composable
fun NetworkScreenPreview() {
    NetworkScreen(
        serverViewModel = hiltViewModel()
    )
}