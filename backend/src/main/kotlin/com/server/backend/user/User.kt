package com.server.backend.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    private var username: String,

    @Column(unique = true)
    var email: String? = null,

    @Column(nullable = false)
    private var password: String,

    @Enumerated(EnumType.STRING)
    val role: UserRole
) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority(role.name))

    override fun getPassword(): String = password

    override fun getUsername(): String = username
}