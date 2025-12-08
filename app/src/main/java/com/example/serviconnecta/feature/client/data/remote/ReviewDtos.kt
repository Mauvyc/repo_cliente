package com.example.serviconnecta.feature.client.data.remote

import com.squareup.moshi.JsonClass

/**
 * DTO para enviar una reseña al backend.
 *
 * Estructura esperada por el endpoint POST /client/reviews
 */
@JsonClass(generateAdapter = true)
data class SubmitReviewRequestDto(
    val booking_id: String,
    val rating: Int,
    val comment: String
)

/**
 * DTO de respuesta al enviar una reseña.
 *
 * Estructura esperada del endpoint POST /client/reviews
 */
@JsonClass(generateAdapter = true)
data class SubmitReviewResponseDto(
    val review_id: String,
    val status: String,
    val created_at: String
)
