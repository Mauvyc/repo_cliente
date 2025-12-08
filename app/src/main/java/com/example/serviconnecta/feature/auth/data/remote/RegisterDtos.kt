package com.example.serviconnecta.feature.auth.data.remote

import com.squareup.moshi.Json

data class RegisterRequestDto(
    @Json(name = "full_name") val fullName: String,
    @Json(name = "email") val email: String,
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "password") val password: String,
    @Json(name = "account_type") val accountType: String,
    @Json(name = "accept_terms") val acceptTerms: Boolean,
    @Json(name = "accept_privacy_policy") val acceptPrivacyPolicy: Boolean
)

data class RegisterResponseDto(
    @Json(name = "user_id") val userId: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "email") val email: String,
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "account_type") val accountType: String,
    @Json(name = "status") val status: UserStatusDto,
    @Json(name = "tokens") val tokens: TokensDto
)