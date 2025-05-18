package ru.hse.routemoodclient.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.map.CreateUserRoute
import ru.hse.routemoodclient.map.ShowMap
import ru.hse.routemoodclient.profile.ProfileSheet
import ru.hse.routemoodclient.screens.LoadingScreen
import ru.hse.routemoodclient.profile.PublishedRoutesScreen
import ru.hse.routemoodclient.profile.RoutesListScreen
import ru.hse.routemoodclient.profile.UserSettingsScreen
import ru.hse.routemoodclient.screens.LoginScreen
import ru.hse.routemoodclient.screens.NetworkScreen
import ru.hse.routemoodclient.screens.RegisterScreen
import ru.hse.routemoodclient.screens.RouteSettings
import ru.hse.routemoodclient.ui.RouteViewModel
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.theme.LightGreen

/**
 * enum values that represent the screens in the app
 */
enum class RouteMoodScreen(@StringRes val title: Int, val color: Color) {
    Start(
        title = R.string.app_name,
        color = LightGreen
    ),
    Map(
        title = R.string.map_screen,
        color = LightGreen
    ),
    SetStart(
        title = R.string.set_start_marker_screen,
        color = LightGreen
    ),
    SetEnd(
        title = R.string.set_end_marker_screen,
        color = LightGreen
    ),
    SetUserRoute(
        title = R.string.set_user_route_screen,
        color = LightGreen
    ),
    Login(
        title = R.string.authorization,
        color = LightGreen
    ),
    Register(
        title = R.string.registration,
        color = LightGreen
    ),
    RouteSettings(
        title = R.string.route_settings,
        color = LightGreen
    ),
    Network(
        title = R.string.network_screen,
        color = LightGreen
    ),
    Loading(
        title = R.string.loading_screen,
    RoutesList(
        title = R.string.routes_list_screen,
        color = LightGreen
    ),
    UserSettings(
        title = R.string.user_settings_screen,
        color = LightGreen
    ),
    PublishedRoutes(
        title = R.string.published_routes_screen,
        color = LightGreen
    )
}

@Composable
fun RouteMoodApp(
    serverViewModel: ServerViewModel = hiltViewModel(),
    routeViewModel: RouteViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    mapsApiKey: String
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = RouteMoodScreen.valueOf(
        backStackEntry?.destination?.route ?: RouteMoodScreen.Login.name
    )

    ProfileSheet(
        currentScreen = currentScreen,
        navController = navController,
        toLoginScreen = {
            navController.navigate(RouteMoodScreen.Login.name) {
                popUpTo(RouteMoodScreen.Login.name) {
                    inclusive = true
                }
            }
        },
        serverViewModel = serverViewModel,
        routeViewModel = routeViewModel
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RouteMoodScreen.Login.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = RouteMoodScreen.Login.name) {
                LoginScreen(
                    serverViewModel = serverViewModel,
                    onLoginButtonClicked = {
                        serverViewModel.askLoginUser()
                        navController.navigate(RouteMoodScreen.RouteSettings.name)
                    },
                    onRegisterButtonClicked = {
                        navController.navigate(RouteMoodScreen.Register.name)
                    }
                )
            }
            composable(route = RouteMoodScreen.Loading.name) {
                LoadingScreen(
                    viewModel = serverViewModel,
                    afterVideo = {
                        navController.navigate(RouteMoodScreen.Map.name)
                    }
                )
            }
            composable(route = RouteMoodScreen.Register.name) {
                RegisterScreen(
                    serverViewModel = serverViewModel,
                    onRegisterButtonClicked = {
                        serverViewModel.askRegisterUser()
                        navController.navigate(RouteMoodScreen.RouteSettings.name)
                    }
                )
            }
            composable(route = RouteMoodScreen.RouteSettings.name) {
                RouteSettings(
                    routeViewModel = routeViewModel,
                    setRouteStart = {
                        navController.navigate(RouteMoodScreen.SetStart.name)
                    },
                    setRouteEnd = {
                        navController.navigate(RouteMoodScreen.SetEnd.name)
                    },
                    setWholeRoute = {
                        navController.navigate(RouteMoodScreen.SetUserRoute.name)
                    },
                    onGenerateButtonClicked = {
                        serverViewModel.askRoute()
                        navController.navigate(RouteMoodScreen.Loading.name)
                    },
                    onDiscardButtonClicked = {
                        routeViewModel.resetRoute()
                    }
                )
            }
            composable(route = RouteMoodScreen.Network.name) {
                NetworkScreen(
                    routeViewModel = routeViewModel,
                    serverViewModel = serverViewModel
                )
            }
            composable(route = RouteMoodScreen.Map.name) {
                ShowMap(
                    viewModel = serverViewModel,
                    onMapClick = {}
                )
            }
            composable(route = RouteMoodScreen.SetStart.name) {
                ShowMap(
                    viewModel = serverViewModel,
                    onMapClick = { latLng: LatLng ->
                        routeViewModel.setStart(latLng.latitude, latLng.longitude)
                    }
                )
            }
            composable(route = RouteMoodScreen.SetEnd.name) {
                ShowMap(
                    viewModel = serverViewModel,
                    onMapClick = { latLng: LatLng ->
                        routeViewModel.setEnd(latLng.latitude, latLng.longitude)
                    }
                )
            }
            composable(route = RouteMoodScreen.SetUserRoute.name) {
                CreateUserRoute(
                    routeViewModel = routeViewModel,
                    mapsApiKey = mapsApiKey
                )
            }
            composable(route = RouteMoodScreen.RoutesList.name) {
                RoutesListScreen(
                    routeViewModel = routeViewModel,
                    onSettingsClicked = {
                        navController.navigate(RouteMoodScreen.RouteSettings.name) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = RouteMoodScreen.UserSettings.name) {
                UserSettingsScreen(
                    serverViewModel = serverViewModel
                )
            }
            composable(route = RouteMoodScreen.PublishedRoutes.name) {
                PublishedRoutesScreen(
                    routeViewModel = routeViewModel,
                    serverViewModel = serverViewModel,
                    onSettingsClicked = {
                        navController.navigate(RouteMoodScreen.RouteSettings.name) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
