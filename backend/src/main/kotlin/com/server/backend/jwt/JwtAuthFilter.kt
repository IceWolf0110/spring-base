package com.server.backend.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService
) : OncePerRequestFilter(
) {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        filterChain: FilterChain?
    ) {
        val authHeader: String? = request?.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain?.doFilter(request, response)
            return
        }

        val token: String = authHeader.substring(7)

        if (!jwtService.isAccessToken(token)) {
            filterChain?.doFilter(request, response)
            return
        }

        val username: String = jwtService.extractUsername(token)

        if (SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (jwtService.isTokenValid(token, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                authToken.details = WebAuthenticationDetailsSource()
                    .buildDetails(request)

                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain?.doFilter(request, response)
    }
}