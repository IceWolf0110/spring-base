package com.server.backend.auth.dto.request

data class LogoutRequest(
    val refreshToken: String
)
