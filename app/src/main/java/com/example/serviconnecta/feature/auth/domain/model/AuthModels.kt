package com.example.serviconnecta.feature.auth.domain.model

data class UserStatus(
    val phoneVerified: Boolean,
    val identityVerified: Boolean,
    val profileCompleted: Boolean
)

data class User(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val accountType: String,
    val status: UserStatus
)

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

data class AuthSession(
    val user: User,
    val tokens: AuthTokens
)