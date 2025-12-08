package com.example.serviconnecta.feature.client.data.remote

/**
 * DTO de respuesta para búsqueda de servicios.
 *
 * Estructura esperada del endpoint GET /services/search
 */
data class SearchServicesResponseDto(
    val query: String,
    val pagination: PaginationDto,
    val services: List<ServiceDto>
)
