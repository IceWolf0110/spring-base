package com.server.backend.auth.dto.response

import com.server.backend.user.dto.response.UserResponse

data class LoginResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val message: String?,
)