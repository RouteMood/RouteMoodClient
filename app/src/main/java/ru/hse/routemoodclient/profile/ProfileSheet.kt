package ru.hse.routemoodclient.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.hse.routemoodclient.navigation.RouteMoodBottomBar
import ru.hse.routemoodclient.navigation.RouteMoodScreen
import ru.hse.routemoodclient.navigation.RouteMoodTopBar
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.theme.LightGreen

@Composable
fun ProfileSheet(
    currentScreen: RouteMoodScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    toMapScreen: () -> Unit,
    toRouteSettings: () -> Unit,
    toNetScreen: () -> Unit,
    serverViewModel: ServerViewModel,
    routeViewModel: RouteViewModel,
    content: @Composable (PaddingValues) -> Unit
) {
    val isDrawerOpen = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                RouteMoodTopBar(
                    currentScreen = currentScreen,
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateUp,
                    actions = {
                        IconButton(onClick = {
                            isDrawerOpen.value = !isDrawerOpen.value
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                RouteMoodBottomBar(
                    currentScreen = currentScreen,
                    toMapScreen = toMapScreen,
                    toRouteSettings = toRouteSettings,
                    toNetScreen = toNetScreen
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }

        AnimatedVisibility(
            visible = isDrawerOpen.value,
            enter = slideInHorizontally { fullWidth -> fullWidth },
            exit = slideOutHorizontally { fullWidth -> fullWidth },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(300.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGreen, shape = RoundedCornerShape(10.dp))
            ) {
                NavHost(navController, startDestination = "profileMenu") {
                    composable("profileMenu") {
                        ProfileScreen(
                            closeSheet = { isDrawerOpen.value = !isDrawerOpen.value },
                            toSavedRoutes = { navController.navigate("routesList") },
                            serverViewModel = serverViewModel
                        )
                    }
                    composable("routesList") {
                        RoutesListScreen(
                            navigateUp = { navController.navigateUp() },
                            routeViewModel = routeViewModel,
                            onSettingsClicked = toRouteSettings
                        )
                    }
                }
            }
        }
    }
}
