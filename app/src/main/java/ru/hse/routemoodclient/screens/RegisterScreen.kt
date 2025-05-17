package ru.hse.routemoodclient.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.components.WhiteButton
import ru.hse.routemoodclient.ui.theme.LightGreen

@Composable
fun RegisterScreen(
    serverViewModel: ServerViewModel = viewModel(),
    onRegisterButtonClicked: () -> Unit
) {
    val userState by serverViewModel.userState.collectAsState()
    Image(
        painter = ColorPainter(LightGreen),
        modifier = Modifier,
        contentDescription = "Background"
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.logo),
            contentDescription = "Logotype",
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = "Welcome to RouteMood!",
            color = Color.White,
            fontSize = 25.sp
        )
        OutlinedTextField(
            value = userState.username,
            onValueChange = { serverViewModel.saveUsername(it) },
            label = { Text("Username") }
        )
        OutlinedTextField(
            value = userState.login,
            onValueChange = { serverViewModel.saveUserLogin(it) },
            label = { Text("Login") }
        )
        OutlinedTextField(
            value = userState.password,
            onValueChange = { serverViewModel.saveUserPassword(it) },
            label = { Text("Password") }
        )
        WhiteButton(
            onClick = onRegisterButtonClicked,
            buttonText = "Register"
        )
    }
}


@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegisterButtonClicked = {}
    )
}