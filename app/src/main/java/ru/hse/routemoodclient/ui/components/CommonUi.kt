package ru.hse.routemoodclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hse.routemoodclient.ui.theme.LightGreen

@Composable
fun ButtonWithTitle (
    title: String,
    onClick: () -> Unit,
    buttonText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp
        )
        GreenButton(
            onClick = onClick,
            buttonText = buttonText
        )
    }
}

@Preview
@Composable
fun ButtonWithTitlePreview() {
    ButtonWithTitle(
        title = "title",
        onClick = {},
        buttonText = "text"
    )
}

@Composable
fun GreenButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonColors(
            containerColor = LightGreen,
            contentColor = Color.White,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.Black
        )
    ) {
        Text(buttonText)
    }
}

@Composable
fun WhiteButton(
    onClick: () -> Unit,
    buttonText: String
) {
    Button(onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.Black
        )
    ) {
        Text(buttonText)
    }
}
