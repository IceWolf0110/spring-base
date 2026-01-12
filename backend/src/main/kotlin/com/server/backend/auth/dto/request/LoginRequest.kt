package com.server.backend.auth.dto.request

data class LoginRequest(
    val username: String,
    val password: String,
)