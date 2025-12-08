package com.example.serviconnecta.feature.auth.data.remote

data class UpdateUserProfileRequestDto(
    val full_name: String?,
    val email: String?,
    val phone_number: String?,
    val avatar_url: String? = null
)

data class UserProfileDto(
    val id: String,
    val full_name: String,
    val email: String,
    val phone_number: String,
    val avatar_url: String? = null,
    val account_type: String
)

data class UpdateUserProfileResponseDto(
    val user: UserProfileDto
)