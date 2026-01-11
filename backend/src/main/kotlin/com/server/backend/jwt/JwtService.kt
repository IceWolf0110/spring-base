package com.server.backend.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey
import kotlin.io.encoding.Base64

@Service
class JwtService {
    private val secretKey: String = generateSecretKey()
    private val accessExpiration: Long = 15 * 60 * 1000
    private val refreshExpiration: Long = 7 * 24 * 60 * 60 * 1000
    private val tokenTypeClaim = "type"
    private val accessTokenType = "access"
    private val refreshTokenType = "refresh"

    fun generateAccessToken(userDetails: UserDetails): String {
        val claims = mapOf(tokenTypeClaim to accessTokenType)
        return buildToken(claims, userDetails, accessExpiration)
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        val claims = mapOf(tokenTypeClaim to refreshTokenType)
        return buildToken(claims, userDetails, refreshExpiration)
    }

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long
    ): String {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey())
            .compact()
    }

    fun isAccessToken(token: String): Boolean {
        return extractClaim(token) { it[tokenTypeClaim] as? String } == accessTokenType
    }

    fun isRefreshToken(token: String): Boolean {
        return extractClaim(token) { it[tokenTypeClaim] as? String } == refreshTokenType
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun getSignInKey(): SecretKey {
        val keyBytes = Base64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun generateSecretKey(): String {
        val key = Jwts.SIG.HS256.key().build()
        return Base64.encode(key.encoded)
    }
}