package ru.hse.routemoodclient.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.components.GreenButton

@Composable
fun UserSettingsScreen(
    serverViewModel: ServerViewModel
) {
    val userState by serverViewModel.userState.collectAsState()
    var userLogin by remember { mutableStateOf(userState.username) }
    var userPassword by remember { mutableStateOf(userState.password) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = userLogin,
            onValueChange = { userLogin = it },
            label = { Text("Change username") }
        )
        OutlinedTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            label = { Text("Change password") }
        )
        Row {
            Spacer(Modifier.weight(1F))
            GreenButton(
                onClick = {
                    // TODO submit to network
                },
                buttonText = "Confirm",
                modifier = Modifier.weight(5F)
            )
            Spacer(Modifier.weight(1F))
            GreenButton(
                onClick = {
                    userLogin = serverViewModel.userState.value.username
                    userPassword = serverViewModel.userState.value.password
                },
                buttonText = "Cancel",
                modifier = Modifier.weight(5F)
            )
            Spacer(Modifier.weight(1F))
        }
    }
}