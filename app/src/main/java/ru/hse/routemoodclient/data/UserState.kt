package ru.hse.routemoodclient.data

import com.google.android.gms.maps.model.LatLng

/**
 * Data class that represents the current user state in terms of [username], [login], [password]
 */
data class UserState(
    val username : String = "testUser",
    val login : String = "testUser",
    val password : String = "passwd",
    val token : String = ""
)