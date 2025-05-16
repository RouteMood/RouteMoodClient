package ru.hse.routemoodclient.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.ui.theme.LightGreen

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteMoodTopBar(
    currentScreen: RouteMoodScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shouldDisplayTopBar = when (currentScreen.title) {
        R.string.authorization, R.string.registration -> false
        else -> true
    }
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = currentScreen.color
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (shouldDisplayTopBar) {
                IconButton(onClick = openProfile) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun RouteMoodTopBarPreview() {
    RouteMoodTopBar(
        currentScreen = RouteMoodScreen.RouteSettings,
        canNavigateBack = true,
        navigateUp = {},
        openProfile = {}
    )
}

/**
 * Composable that displays the bottomBar and displays settings, map, network buttons.
 */
@Composable
fun RouteMoodBottomBar(
    currentScreen: RouteMoodScreen,
    toMapScreen: () -> Unit,
    toRouteSettings: () -> Unit,
    toNetScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shouldDisplayBottomBar = when (currentScreen.title) {
        R.string.authorization, R.string.registration -> false
        else -> true
    }

    BottomAppBar(
        containerColor = currentScreen.color,
        modifier = modifier
    ) {
        if (shouldDisplayBottomBar) {
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = toRouteSettings,
                modifier = Modifier
                    .background(LightGreen)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.route_settings_icon),
                    contentDescription = stringResource(R.string.route_settings_button)
                )
            }
            Spacer(Modifier.weight(3f))
            IconButton(
                onClick = toMapScreen,
                modifier = Modifier
                    .background(LightGreen)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.map_screen_icon),
                    contentDescription = stringResource(R.string.map_screen_button)
                )
            }
            Spacer(Modifier.weight(3f))
            IconButton(
                onClick = toNetScreen,
                modifier = Modifier
                    .background(LightGreen)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.net_screen_icon),
                    contentDescription = stringResource(R.string.net_screen_button),
                    modifier = Modifier.size(25.dp)
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun RouteMoodBottomBarPreview() {
    RouteMoodBottomBar(
        currentScreen = RouteMoodScreen.RouteSettings,
        toMapScreen = {},
        toRouteSettings = {},
        toNetScreen = {}
    )
}