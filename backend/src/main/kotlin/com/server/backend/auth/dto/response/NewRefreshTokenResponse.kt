package com.server.backend.auth.dto.response

data class NewRefreshTokenResponse (
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val message: String? = null
)