package com.server.backend.auth.dto

data class LoginRequest(
    val username: String,
    val password: String,
)