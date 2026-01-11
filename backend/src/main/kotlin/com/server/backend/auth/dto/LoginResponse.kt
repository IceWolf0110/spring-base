package com.server.backend.auth.dto

import com.server.backend.user.dto.UserResponse

data class LoginResponse(
    val token: String?,
    val message: String?,
    val user: UserResponse?,
)