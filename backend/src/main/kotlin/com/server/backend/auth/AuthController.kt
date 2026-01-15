package com.server.backend.auth

import com.server.backend.auth.dto.request.LoginRequest
import com.server.backend.auth.dto.response.LoginResponse
import com.server.backend.auth.dto.request.RefreshTokenRequest
import com.server.backend.auth.dto.response.NewRefreshTokenResponse
import com.server.backend.auth.dto.request.RegisterRequest
import com.server.backend.auth.dto.response.RegisterResponse
import com.server.backend.auth.dto.response.ValidateRefreshTokenResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    fun login(
        @RequestBody request: LoginRequest
    ) : ResponseEntity<LoginResponse> {
        return authService.login(request)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest
    ) : ResponseEntity<NewRefreshTokenResponse> {
        return authService.refreshToken(request)
    }

    @PostMapping("/validate-refresh-token")
    fun validateRefreshToken(
        @RequestBody request: RefreshTokenRequest
    ) : ResponseEntity<ValidateRefreshTokenResponse> {
        return authService.validateRefreshToken(request)
    }
}