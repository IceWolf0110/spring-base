package com.server.backend.auth

import com.server.backend.auth.dto.*
import com.server.backend.jwt.JwtService
import com.server.backend.user.User
import com.server.backend.user.UserRepo
import com.server.backend.user.UserRole
import com.server.backend.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    val userRepo: UserRepo,
    val userService: UserService,
    val userDetailsService: UserDetailsService,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) {
    private val encoder = BCryptPasswordEncoder()

    fun register(request: RegisterRequest): ResponseEntity<RegisterResponse> {
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

        val user = User(
            username = request.username,
            password = encoder.encode(request.password),
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

    fun login(request: LoginRequest): ResponseEntity<LoginResponse> {
        if (request.password.isEmpty() || request.username.isEmpty() ) {
            return ResponseEntity.badRequest().body(LoginResponse(
                token = null,
                message = "Invalid username or password",
                user = null
            ))
        }

        val username = request.username

        val user = userRepo.findByUsername(username) ?: return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                LoginResponse(
                    token = null,
                    message = "User not found!",
                    user = null
                )
            )

        val password = request.password

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                username,
                password
            )
        )

        val accessToken = jwtService.generateAccessToken(user)

        return ResponseEntity.ok(
            LoginResponse(
                token = accessToken,
                message = "User login successful!",
                user = userService.toUserResponse(user)
            )
        )
    }

    fun refreshToken(request: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {
        val refreshToken = request.refreshToken

        if (refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body(RefreshTokenResponse())
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            return ResponseEntity.badRequest().body(RefreshTokenResponse())
        }

        val username = jwtService.extractUsername(refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(username)

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            return ResponseEntity.status(401).body(
                RefreshTokenResponse(message = "Invalid or expired refresh token")
            )
        }

        val expiration = jwtService.extractExpiration(refreshToken)

        val newRefreshToken = if (expiration.time - Date().time <= 60 * 60 * 1000) {
            jwtService.generateRefreshToken(userDetails)
        } else {
            null
        }

        val newAccessToken = jwtService.generateAccessToken(userDetails)

        return ResponseEntity.ok(
            RefreshTokenResponse(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
                message = "Tokens refreshed successfully"
            )
        )
    }
}