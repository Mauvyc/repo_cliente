package com.example.serviconnecta.feature.auth.data.remote

import com.squareup.moshi.Json

data class LoginRequestDto(
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "password") val password: String
)

data class UserStatusDto(
    @Json(name = "phone_verified") val phoneVerified: Boolean,
    @Json(name = "identity_verified") val identityVerified: Boolean,
    @Json(name = "profile_completed") val profileCompleted: Boolean
)

data class UserDto(
    @Json(name = "id") val id: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "email") val email: String,
    @Json(name = "account_type") val accountType: String,
    @Json(name = "status") val status: UserStatusDto
)

data class TokensDto(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "expires_in") val expiresIn: Long
)

data class LoginResponseDto(
    @Json(name = "user") val user: UserDto,
    @Json(name = "tokens") val tokens: TokensDto
)