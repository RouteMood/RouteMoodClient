package ru.hse.routemoodclient.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.screens.LoginScreen

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteMoodTopBar(
    currentScreen: RouteMoodScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        }
    )
}

@Preview
@Composable
fun RouteMoodTopBarPreview() {
    RouteMoodTopBar(
        currentScreen = RouteMoodScreen.RouteSettings,
        canNavigateBack = true,
        navigateUp = {}
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
    BottomAppBar(
        containerColor = currentScreen.color,
        modifier = modifier
    ) {
        val shouldDisplayBottomBar = when (currentScreen.title) {
            R.string.map_screen, R.string.route_settings -> true
            else -> false
        }
        if (shouldDisplayBottomBar) {
            Spacer(Modifier.weight(0.25f, true))
            IconButton(onClick = toRouteSettings) {
                Icon(
                    painter = painterResource(id = R.drawable.route_settings_icon),
                    contentDescription = stringResource(R.string.route_settings_button)
                )
            }
            Spacer(Modifier.weight(1f, true))
            IconButton(onClick = toMapScreen) {
                Icon(
                    painter = painterResource(id = R.drawable.map_screen_icon),
                    contentDescription = stringResource(R.string.map_screen_button)
                )
            }
            Spacer(Modifier.weight(1f, true))
            IconButton(onClick = toNetScreen) {
                Icon(
                    painter = painterResource(id = R.drawable.net_screen_icon),
                    contentDescription = stringResource(R.string.net_screen_button)
                )
            }
            Spacer(Modifier.weight(0.25f, true))
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