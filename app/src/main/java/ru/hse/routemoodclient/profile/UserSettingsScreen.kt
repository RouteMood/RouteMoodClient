package ru.hse.routemoodclient.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import ru.hse.routemoodclient.R
import ru.hse.routemoodclient.data.UserState
import ru.hse.routemoodclient.ui.ServerViewModel
import ru.hse.routemoodclient.ui.components.GreenButton
import ru.hse.routemoodclient.ui.theme.LightGreen
import java.io.File
import java.util.UUID

@Composable
fun UserSettingsScreen(
    serverViewModel: ServerViewModel
) {
    val userState by serverViewModel.userState.collectAsState()
    var userLogin by remember { mutableStateOf(userState.username) }
    var userPassword by remember { mutableStateOf(userState.password) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val images by serverViewModel.images.collectAsState()
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { serverViewModel.saveProfileImage(it) } }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ProfileImagePainter(userState.profileImageId, serverViewModel)
        }
        if (images[userState.profileImageId] != null && !serverViewModel.isRefreshing) {
            item {
                Row (
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Spacer(Modifier.weight(1F))
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGreen,
                            contentColor = Color.White
                        ),
                        enabled = !serverViewModel.isRefreshing,
                        modifier = Modifier.weight(5F)
                    ) {
                        Text("Upload Photo")
                    }
                    Spacer(Modifier.weight(1F))
                    Button(
                        onClick = { serverViewModel.deleteProfileImage() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.weight(5F)
                    ) {
                        Text("Delete Photo")
                    }
                    Spacer(Modifier.weight(1F))
                }
            }
        }
        else {
            item {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.padding(vertical = 8.dp),
                    enabled = !serverViewModel.isRefreshing
                ) {
                    Text(if (serverViewModel.isRefreshing) "Loading..." else "Upload Photo")
                }
            }
        }
        item {
            OutlinedTextField(
                value = userLogin,
                onValueChange = { userLogin = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                label = {
                    Text(
                        "Change username",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            )
        }
        item {
            OutlinedTextField(
                value = userPassword,
                onValueChange = { userPassword = it },
                label = {
                    Text(
                        "Change password",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Search
                            else Icons.Default.Build,
                            contentDescription = if (isPasswordVisible) "Hide password"
                            else "Show password",
                            tint = Color.Black
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
        }
        item {
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
}

@Composable
fun ProfileImagePainter(
    profileImageId: UUID?,
    serverViewModel: ServerViewModel
) {
    val images by serverViewModel.images.collectAsState()
    if (profileImageId != null) {
        serverViewModel.loadImageFromServer(profileImageId)
    }
    Box(
        modifier = Modifier
            .size(200.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(50.dp)),
        contentAlignment = Alignment.Center
    ) {
        when {
            serverViewModel.isRefreshing -> {
                CircularProgressIndicator()
            }
            profileImageId != null && images[profileImageId] != null -> {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = images[profileImageId]
                    ),
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Empty Profile",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}