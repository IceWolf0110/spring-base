package com.server.backend.auth

import com.server.backend.auth.dto.request.LoginRequest
import com.server.backend.auth.dto.request.LogoutRequest
import com.server.backend.auth.dto.response.LoginResponse
import com.server.backend.auth.dto.request.RefreshTokenRequest
import com.server.backend.auth.dto.response.RefreshTokenResponse
import com.server.backend.auth.dto.request.RegisterRequest
import com.server.backend.auth.dto.response.LogoutResponse
import com.server.backend.auth.dto.response.RegisterResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    val authService: AuthService,
) {
    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest
    ) : ResponseEntity<RegisterResponse> {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) : ResponseEntity<LoginResponse> {
        return authService.login(request)
    }

    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody request: LogoutRequest
    ) : ResponseEntity<LogoutResponse> {
        return authService.logout(authHeader, request)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest
    ) : ResponseEntity<RefreshTokenResponse> {
        return authService.refreshToken(request)
    }
}