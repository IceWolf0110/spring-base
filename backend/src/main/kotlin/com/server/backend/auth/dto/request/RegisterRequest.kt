package com.server.backend.auth.dto.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)