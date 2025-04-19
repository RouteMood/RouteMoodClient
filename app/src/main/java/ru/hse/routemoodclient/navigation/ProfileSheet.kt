package ru.hse.routemoodclient.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import ru.hse.routemoodclient.screens.ProfileScreen
import ru.hse.routemoodclient.ui.theme.LightGreen

@Composable
fun ProfileSheet(
    currentScreen: RouteMoodScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    toMapScreen: () -> Unit,
    toRouteSettings: () -> Unit,
    toNetScreen: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = LightGreen,
                drawerContentColor = LightGreen
            ) {
                ProfileScreen()
            }
        },
        scrimColor = LightGreen,
    ) {
        Scaffold(
            topBar = {
                RouteMoodTopBar(
                    currentScreen = currentScreen,
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateUp,
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                RouteMoodBottomBar (
                    currentScreen = currentScreen,
                    toMapScreen = toMapScreen,
                    toRouteSettings = toRouteSettings,
                    toNetScreen = toNetScreen
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}