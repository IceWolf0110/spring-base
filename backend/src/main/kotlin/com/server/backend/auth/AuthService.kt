package com.server.backend.auth

import com.server.backend.auth.dto.request.LoginRequest
import com.server.backend.auth.dto.request.RefreshTokenRequest
import com.server.backend.auth.dto.request.RegisterRequest
import com.server.backend.auth.dto.response.LoginResponse
import com.server.backend.auth.dto.response.NewRefreshTokenResponse
import com.server.backend.auth.dto.response.RegisterResponse
import com.server.backend.auth.dto.response.ValidateRefreshTokenResponse
import com.server.backend.jwt.JwtService
import com.server.backend.user.User
import com.server.backend.user.UserRepo
import com.server.backend.user.UserRole
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import kotlin.text.isEmpty
import kotlin.time.Duration.Companion.hours

@Service
class AuthService(
    val userRepo: UserRepo,
    val userDetailsService: UserDetailsService,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) {
    private val encoder = BCryptPasswordEncoder()

    fun register(
        request: RegisterRequest
    ) : ResponseEntity<RegisterResponse> {
        if (request.password.isEmpty() || request.username.isEmpty() ) {
            return ResponseEntity.badRequest().body(RegisterResponse())
        }

        if (userRepo.findByUsername(request.username) != null) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                    RegisterResponse(
                        message = "User already exists!",
                    )
                )
        }

        if (userRepo.findByEmail(request.email) != null) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                    RegisterResponse(
                        message = "Email is already taken!",
                    )
                )
        }

        val user = User (
            username = request.username,
            password = encoder.encode(request.password).toString(),
            email = request.email,
            role = UserRole.USER,
        )

        userRepo.save(user)

        return ResponseEntity.ok(
            RegisterResponse(
                message = "User register successful!"
            )
        )
    }

    fun login(
        request: LoginRequest
    ) : ResponseEntity<LoginResponse> {
        if (request.password.isEmpty() || request.username.isEmpty() ) {
            return ResponseEntity.badRequest().body(
                LoginResponse(
                    accessToken = null,
                    refreshToken = null,
                    message = "Invalid username or password",
                )
            )
        }

        val username = request.username

        val user = userRepo.findByUsername(username) ?: return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                LoginResponse(
                    accessToken = null,
                    refreshToken = null,
                    message = "User not found!",
                )
            )

        val password = request.password

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                username,
                password
            )
        )

        val refreshToken = jwtService.generateRefreshToken(user)
        val accessToken = jwtService.generateAccessToken(user)

        return ResponseEntity.ok(
            LoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                message = "User login successful!",
            )
        )
    }

    fun refreshToken(
        request: RefreshTokenRequest
    ) : ResponseEntity<NewRefreshTokenResponse> {
        val refreshToken = request.refreshToken

        if (!isTokenRefreshValid(refreshToken)) {
            return ResponseEntity.status(401).body(
                NewRefreshTokenResponse(message = "Invalid or expired refresh token")
            )
        }

        val username = jwtService.extractUsername(refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(username)
        val expiration = jwtService.extractExpiration(refreshToken)

        val newRefreshToken = if (expiration.time - Date().time <= 1.hours.inWholeMilliseconds) {
            jwtService.generateRefreshToken(userDetails)
        } else {
            null
        }

        val newAccessToken = jwtService.generateAccessToken(userDetails)

        return ResponseEntity.ok(
            NewRefreshTokenResponse(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
                message = "Tokens refreshed successfully"
            )
        )
    }

    fun validateRefreshToken(
        request: RefreshTokenRequest
    ): ResponseEntity<ValidateRefreshTokenResponse> {
        return ResponseEntity.ok(
            ValidateRefreshTokenResponse(
                isValidToken = isTokenRefreshValid(request.refreshToken),
            )
        )
    }

    private fun isTokenRefreshValid(token: String): Boolean {
        if (token.isEmpty()) {
            return false
        }

        if (!jwtService.isRefreshToken(token)) {
            return false
        }

        val username = jwtService.extractUsername(token)
        val userDetails = userDetailsService.loadUserByUsername(username)

        return jwtService.isTokenValid(token, userDetails)
    }
}