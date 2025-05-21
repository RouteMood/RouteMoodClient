package ru.hse.routemoodclient.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.components.GreenButton

@Composable
fun ProfileScreen(
    toSavedRoutes: () -> Unit,
    toSharedRoutes: () -> Unit,
    toSettings: () -> Unit,
    logout: () -> Unit,
    serverViewModel: ServerViewModel
) {
    val userState by serverViewModel.userState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = userState.username,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GreenButton(
                    onClick = toSavedRoutes,
                    buttonText = "Saved routes",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                GreenButton(
                    onClick = toSharedRoutes,
                    buttonText = "Shared routes",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                GreenButton(
                    onClick = toSettings,
                    buttonText = "Settings",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
        item {
            IconButton(
                onClick = logout,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logout_icon),
                    contentDescription = "logout icon",
                    modifier = Modifier.size(32.dp),
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        toSavedRoutes = {},
        toSharedRoutes = {},
        toSettings = {},
        logout = {},
        serverViewModel = hiltViewModel()
    )
}