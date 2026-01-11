package com.server.backend.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepo : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String):  User?
}