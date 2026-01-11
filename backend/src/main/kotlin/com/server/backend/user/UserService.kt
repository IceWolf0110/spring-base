package com.server.backend.user

import com.server.backend.user.dto.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo
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
}