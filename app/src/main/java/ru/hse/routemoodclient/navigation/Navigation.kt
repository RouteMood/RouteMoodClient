package ru.hse.routemoodclient.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.map.ShowMap
import ru.hse.routemoodclient.screens.LoginScreen
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
    )
}

@Composable
fun RouteMoodApp(
    serverViewModel: ServerViewModel = hiltViewModel(),
    viewModel: RouteViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = RouteMoodScreen.valueOf(
        backStackEntry?.destination?.route ?: RouteMoodScreen.Start.name
    )

    ProfileSheet(
        currentScreen = currentScreen,
        canNavigateBack = navController.previousBackStackEntry != null,
        navigateUp = { navController.navigateUp() },
        toMapScreen = {
            navController.navigate(RouteMoodScreen.Map.name) {
                launchSingleTop = true
            }
        },
        toRouteSettings = {
            navController.navigate(RouteMoodScreen.RouteSettings.name) {
                launchSingleTop = true
            }
        },
        toNetScreen = {}
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RouteMoodScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = RouteMoodScreen.Start.name) {
                LoginScreen (
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
            composable(route = RouteMoodScreen.Register.name) {
                RegisterScreen (
                    serverViewModel = serverViewModel,
                    onRegisterButtonClicked = {
                        serverViewModel.askRegisterUser()
                        navController.navigate(RouteMoodScreen.RouteSettings.name)
                    }
                )
            }
            composable(route = RouteMoodScreen.RouteSettings.name) {
                RouteSettings (
                    viewModel = viewModel,
                    setRouteStart = {
                        navController.navigate(RouteMoodScreen.SetStart.name)
                    },
                    setRouteEnd = {
                        navController.navigate(RouteMoodScreen.SetEnd.name)
                    },
                    onGenerateButtonClicked = {
                        //serverViewModel.askRoute()
                        serverViewModel.askFictiveRoute()
                        navController.navigate(RouteMoodScreen.Map.name)
                    },
                    onDiscardButtonClicked = {
                        viewModel.resetRoute()
                    }
                )
            }
            composable(route = RouteMoodScreen.Map.name) {
                ShowMap(
                    viewModel = viewModel,
                    onMapClick = {}
                )
            }
            composable(route = RouteMoodScreen.SetStart.name) {
                ShowMap(
                    viewModel = viewModel,
                    onMapClick = {
                            latLng: LatLng ->
                        viewModel.setStart(latLng.latitude, latLng.longitude)
                    }
                )
            }
            composable(route = RouteMoodScreen.SetEnd.name) {
                ShowMap(
                    viewModel = viewModel,
                    onMapClick = {
                            latLng: LatLng ->
                        viewModel.setEnd(latLng.latitude, latLng.longitude)
                    }
                )
            }
        }
    }
}


/**
 * Resets the [RouteUiState] and pops up to [RouteMoodScreen.Start]
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: RouteViewModel,
    navController: NavHostController
) {
    viewModel.resetRoute()
    navController.popBackStack(RouteMoodScreen.Start.name, inclusive = false)
}