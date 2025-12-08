package com.example.serviconnecta.feature.client.data.remote

data class LocationsResponseDto(
    val locations: List<LocationDto>
)

data class LocationDto(
    val id: String? = null,      // en /client/locations (GET)
    val _id: String? = null,     // en /client/locations (POST)
    val label: String,
    val full_address: String,
    val latitude: Double?,
    val longitude: Double?,
    val is_default: Boolean
)

data class CreateLocationRequestDto(
    val label: String,
    val full_address: String,
    val latitude: Double?,
    val longitude: Double?,
    val set_as_default: Boolean
)