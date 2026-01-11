package com.server.backend.config

import com.server.backend.user.UserRepo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Configuration
class ApplicationConfig(
    private val userRepo: UserRepo
) {
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userRepo.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")
        }
    }
}