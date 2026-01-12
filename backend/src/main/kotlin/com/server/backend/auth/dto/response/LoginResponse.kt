package com.server.backend.auth.dto.response

import com.server.backend.user.dto.UserResponse

data class LoginResponse(
    val refreshToken: String?,
    val accessToken: String?,
    val message: String?,
    val user: UserResponse?,
)