package com.example.serviconnecta.feature.auth.data.remote

import com.squareup.moshi.Json

data class VerifyPhoneRequestDto(
    @Json(name = "phone_number") val phoneNumber: String
)

data class VerifyPhoneRequestResponseDto(
    @Json(name = "masked_phone") val maskedPhone: String,
    @Json(name = "expires_in") val expiresIn: Long,
    @Json(name = "attempts_left") val attemptsLeft: Int?
)

data class VerifyPhoneConfirmDto(
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "code") val code: String
)

data class VerifyPhoneConfirmResponseDto(
    @Json(name = "user_id") val userId: String,
    @Json(name = "phone_verified") val phoneVerified: Boolean
)