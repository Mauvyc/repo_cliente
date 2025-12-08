package com.example.serviconnecta.feature.client.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
// GET /client/locations
data class ClientLocationsResponseDto(
    val locations: List<LocationDto>
)

// GET /client/payment-methods
data class ClientPaymentMethodsResponseDto(
    val payment_methods: List<PaymentMethodDto>
)

// POST /client/service-request (entrada)
data class CreateServiceRequestDto(
    val service_id: String,
    val location_id: String,
    val scheduled_date: String,
    val scheduled_time_range: ScheduledTimeRangeDto,
    val payment_method_id: String,
    val notes: String?,
    val price_summary: PriceSummaryDto
)

data class ScheduledTimeRangeDto(
    val start: String,
    val end: String
)

data class PriceSummaryDto(
    val currency: String,
    val subtotal: Double,
    val discount: Double,
    val total: Double
)

// Respuesta de /client/service-request (data)
data class ServiceRequestCreatedDto(
    val request_id: String,
    val status: String
    // Si necesitas más campos, los agregas
)