package ru.hse.routemoodclient.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.navigation.RouteMoodBottomBar
import ru.hse.routemoodclient.navigation.RouteMoodScreen
import ru.hse.routemoodclient.navigation.RouteMoodTopBar
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.theme.LightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSheet(
    currentScreen: RouteMoodScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    toLoginScreen: () -> Unit,
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
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "profileMenu",
                    modifier = Modifier.padding(15.dp).fillMaxSize()
                ) {
                    composable("profileMenu") {
                        Column {
                            IconButton(onClick = { isDrawerOpen.value = !isDrawerOpen.value }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = stringResource(R.string.back_button)
                                )
                            }
                            ProfileScreen(
                                toSavedRoutes = { navController.navigate("routesList") },
                                toSharedRoutes = { navController.navigate("publishedRoutes") },
                                toSettings = { navController.navigate("userSettings") },
                                logout = {
                                    serverViewModel.resetUser()
                                    isDrawerOpen.value = !isDrawerOpen.value
                                    toLoginScreen()
                                },
                                serverViewModel = serverViewModel
                            )
                        }

                    }
                    composable("routesList") {
                        Column {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back_button)
                                )
                            }
                            RoutesListScreen(
                                routeViewModel = routeViewModel,
                                onSettingsClicked = toRouteSettings
                            )
                        }

                    }
                    composable("userSettings") {
                        Column {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back_button)
                                )
                            }
                            UserSettingsScreen(
                                serverViewModel = serverViewModel
                            )
                        }
                    }
                    composable("publishedRoutes") {
                        Column {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back_button)
                                )
                            }
                            PublishedRoutesScreen(
                                routeViewModel = routeViewModel,
                                serverViewModel = serverViewModel,
                                onSettingsClicked = toRouteSettings
                            )
                        }
                    }
                }
            }
        }
    }
}
