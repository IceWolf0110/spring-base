package com.server.backend.auth.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)