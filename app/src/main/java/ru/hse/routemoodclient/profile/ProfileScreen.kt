package ru.hse.routemoodclient.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.components.GreenButton

@Composable
fun ProfileScreen(
    closeSheet : () -> Unit,
    toSavedRoutes : () -> Unit,
    serverViewModel: ServerViewModel
) {
    val userState by serverViewModel.userState.collectAsState()
    Scaffold() {
        LazyColumn(
            modifier = Modifier.padding(it).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box (modifier = Modifier.fillParentMaxWidth()) {
                    IconButton(onClick = closeSheet) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            }
            item {
                Text(userState.username)
            }
            item {
                GreenButton(
                    onClick = toSavedRoutes,
                    buttonText = "Saved routes"
                )
            }
            item {
                GreenButton(
                    onClick = {},
                    buttonText = "Settings"
                )
            }
            item {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logout_icon),
                        contentDescription = "logout icon",
                        modifier = Modifier.requiredSize(30.dp)
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        closeSheet = {},
        toSavedRoutes = {},
        serverViewModel = hiltViewModel()
    )
}