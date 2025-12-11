package com.example.serviconnecta.feature.client.data.remote

data class ClientReservationsResponseDto(
    val pagination: ReservationPaginationDto,
    val requests: List<ServiceRequestDto>
)

data class ReservationPaginationDto(
    val page: Int?,
    val page_size: Int?,
    val total_items: Int,
    val total_pages: Int?
)

data class ServiceRequestDto(
    val request_id: String,
    val service_title: String,
    val service_id: String,
    val provider_name: String,
    val status: String,
    val scheduled_date: String,
    val time_range: TimeRangeDto,
    val total: Double,
    val currency: String,
    val has_review: Boolean? = null  // Indica si ya tiene reseña (opcional)
)

data class TimeRangeDto(
    val start: String,
    val end: String
)