package com.example.serviconnecta.feature.auth.data.remote

data class ChangePasswordRequestDto(
    val current_password: String,
    val new_password: String
)

data class ChangePasswordResponseDto(
    val dummy: String? = null
)
