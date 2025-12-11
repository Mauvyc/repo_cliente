package com.example.serviconnecta.feature.client.data.remote

import com.squareup.moshi.JsonClass

/**
 * DTO para enviar una reseña al backend.
 *
 * Estructura esperada por el endpoint POST /client/service-request/{request_id}/review
 */
@JsonClass(generateAdapter = true)
data class SubmitReviewRequestDto(
    val service_rating: Int,      // Estrellas del servicio (1-5)
    val provider_rating: Int,      // Estrellas del técnico/proveedor (1-5)
    val highlights: List<String>,  // Aspectos destacados
    val comment: String            // Comentario
)

/**
 * DTO de respuesta al enviar una reseña.
 *
 * Estructura esperada del endpoint POST /client/service-request/{request_id}/review
 */
@JsonClass(generateAdapter = true)
data class SubmitReviewResponseDto(
    val review_id: String,
    val request_id: String,
    val service_id: String,
    val provider_id: String,
    val service_rating: Int,
    val provider_rating: Int,
    val highlights: List<String>,
    val comment: String,
    val created_at: String
)
