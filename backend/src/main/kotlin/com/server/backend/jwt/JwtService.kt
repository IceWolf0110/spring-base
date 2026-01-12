package com.server.backend.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey
import kotlin.io.encoding.Base64
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

@Service
class JwtService(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val secretKey: String = getSecretKey()
    private val accessExpiration: Long = 15.minutes.inWholeMilliseconds
    private val refreshExpiration: Long = 30.days.inWholeMilliseconds
    private val tokenTypeClaim = "type"
    private val accessTokenType = "access"
    private val refreshTokenType = "refresh"
    private val blacklistPrefix = "token:blacklist:"

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

    private fun getSecretKey(): String {
        val key = System.getenv("JWT_SECRET_KEY")
        return Base64.encode(key.encodeToByteArray())
    }

    fun blacklistToken(token: String, expiration: Date) {
        val ttl = expiration.time - System.currentTimeMillis()

        if (ttl > 0) {
            redisTemplate.opsForValue().set(
                "$blacklistPrefix$token",
                "true",
                ttl,
                TimeUnit.MILLISECONDS
            )
        }
    }

    fun isBlacklistedToken(token: String): Boolean {
        return redisTemplate.hasKey("$blacklistPrefix$token") ?: false
    }

    fun removeTokenFromBlacklist(token: String) {
        redisTemplate.delete("$blacklistPrefix$token")
    }
}