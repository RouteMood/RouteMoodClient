package ru.hse.routemoodclient.data

import java.util.UUID

/**
 * Data class that represents the current user state in terms of [username], [login], [password]
 */
data class UserState(
    val username : String = "testUser",
    val login : String = "testUser",
    val password : String = "passwd",
    val token : String = "",
    val profileImageId : UUID? = null
)