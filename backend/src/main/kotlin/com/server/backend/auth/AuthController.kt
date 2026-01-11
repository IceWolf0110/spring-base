package com.server.backend.auth

import com.server.backend.auth.dto.LoginRequest
import com.server.backend.auth.dto.LoginResponse
import com.server.backend.auth.dto.RefreshTokenRequest
import com.server.backend.auth.dto.RefreshTokenResponse
import com.server.backend.auth.dto.RegisterRequest
import com.server.backend.auth.dto.RegisterResponse
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
    fun register(@RequestBody request: RegisterRequest) : ResponseEntity<RegisterResponse> {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) : ResponseEntity<LoginResponse> {
        return authService.login(request)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest) : ResponseEntity<RefreshTokenResponse> {
        return authService.refreshToken(request)
    }
}