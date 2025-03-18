package ru.hse.routemoodclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.hse.routemoodclient.navigation.RouteMoodApp
import ru.hse.routemoodclient.ui.theme.RouteMoodClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RouteMoodClientTheme {
                RouteMoodApp()
            }
        }
    }
}