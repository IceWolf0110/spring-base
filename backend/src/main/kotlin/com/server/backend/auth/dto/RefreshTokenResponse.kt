package com.server.backend.auth.dto

data class RefreshTokenResponse (
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val message: String? = null
)