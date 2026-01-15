package com.server.backend.user

import com.server.backend.auth.dto.request.LogoutRequest
import com.server.backend.user.dto.response.LogoutResponse
import com.server.backend.jwt.JwtService
import com.server.backend.user.dto.response.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo,
    private  val jwtService: JwtService
) {
    fun getUserList(role: UserRole? = null): ResponseEntity<List<UserResponse>> {
        val users = userRepo.findAll()
            .filter { role == null || it.role == role }
            .map(::toUserResponse)

        return ResponseEntity.ok(users)
    }

    fun getUserById(id: Long): ResponseEntity<UserResponse> {
        val user = userRepo.findById(id).orElse(null) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(toUserResponse(user))
    }

    fun getUserByUsername(username: String): ResponseEntity<UserResponse> {
        val user = userRepo.findByUsername(username) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(toUserResponse(user))
    }

    fun getUserByEmail(email: String): ResponseEntity<UserResponse> {
        val user = userRepo.findByEmail(email) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(toUserResponse(user))
    }

    fun toUserResponse(user: User) = UserResponse(
        username = user.username,
        email = user.email,
        role = user.role.toString()
    )

    fun logout(
        authHeader: String,
        request: LogoutRequest
    ) : ResponseEntity<LogoutResponse> {
        val accessToken = authHeader.substring(7)

        jwtService.blacklistToken(
            accessToken,
            jwtService.extractExpiration(accessToken)
        )

        request.refreshToken.let { refreshToken ->
            jwtService.blacklistToken(
                refreshToken,
                jwtService.extractExpiration(refreshToken)
            )
        }

        return ResponseEntity.ok(
            LogoutResponse(
                message = "Logged out successfully"
            )
        )
    }
}