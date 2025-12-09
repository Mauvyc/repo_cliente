package com.example.serviconnecta.feature.worker.data.remote

import com.example.serviconnecta.feature.worker.domain.model.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Request para obtener servicios con paginación
@JsonClass(generateAdapter = true)
data class GetServicesRequest(
    @Json(name = "page") val page: Int = 1,
    @Json(name = "page_size") val pageSize: Int = 10,
    @Json(name = "status") val status: String = "ALL" // ALL | ACTIVE | PAUSED
)

// Response de servicios con paginación
@JsonClass(generateAdapter = true)
data class ServicesResponse(
    @Json(name = "pagination") val pagination: PaginationDto,
    @Json(name = "services") val services: List<ServiceDto>,
    @Json(name = "reservations_summary") val reservationsSummary: List<ReservationSummaryDto>
)

@JsonClass(generateAdapter = true)
data class PaginationDto(
    @Json(name = "page") val page: Int,
    @Json(name = "page_size") val pageSize: Int,
    @Json(name = "total_items") val totalItems: Int,
    @Json(name = "total_pages") val totalPages: Int
)

@JsonClass(generateAdapter = true)
data class ServiceDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "category") val category: ServiceCategoryDto,
    @Json(name = "price") val price: Double,
    @Json(name = "currency") val currency: String,
    @Json(name = "status") val status: String, // ACTIVE | PAUSED
    @Json(name = "pending_requests_count") val pendingRequestsCount: Int,
    @Json(name = "image_url") val imageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class ServiceCategoryDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class ReservationSummaryDto(
    @Json(name = "request_id") val requestId: String,
    @Json(name = "service_title") val serviceTitle: String,
    @Json(name = "client_name") val clientName: String,
    @Json(name = "scheduled_date") val scheduledDate: String,
    @Json(name = "time_range") val timeRange: TimeRangeDto,
    @Json(name = "status") val status: String
)

// TimeRangeDto ya está definido en WorkerHomeResponseDto.kt en este mismo paquete

// Request para crear servicio
@JsonClass(generateAdapter = true)
data class CreateServiceRequest(
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "category_id") val categoryId: String,
    @Json(name = "price") val price: Double,
    @Json(name = "currency") val currency: String = "PEN",
    @Json(name = "media") val media: MediaDto? = null
)

@JsonClass(generateAdapter = true)
data class MediaDto(
    @Json(name = "image_base64") val imageBase64: String
)

// Response de crear servicio
@JsonClass(generateAdapter = true)
data class CreateServiceResponse(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "category") val category: ServiceCategoryDto,
    @Json(name = "price") val price: Double,
    @Json(name = "currency") val currency: String,
    @Json(name = "status") val status: String,
    @Json(name = "image_url") val imageUrl: String?
)

// Request para editar servicio
@JsonClass(generateAdapter = true)
data class UpdateServiceRequest(
    @Json(name = "title") val title: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "price") val price: Double? = null,
    @Json(name = "status") val status: String? = null // ACTIVE | PAUSED
)

// Mappers de DTO a modelos de dominio
object ServicesMappers {
    fun ServiceDto.toDomain(): Service {
        return Service(
            id = id,
            title = title,
            description = description,
            category = ServiceCategory(
                id = category.id,
                name = category.name
            ),
            price = price,
            currency = currency,
            status = when (status) {
                "ACTIVE" -> ServiceStatus.ACTIVE
                "PAUSED" -> ServiceStatus.PAUSED
                else -> ServiceStatus.ACTIVE
            },
            pendingRequestsCount = pendingRequestsCount,
            imageUrl = imageUrl
        )
    }

    fun CreateServiceResponse.toDomain(): Service {
        return Service(
            id = id,
            title = title,
            description = description,
            category = ServiceCategory(
                id = category.id,
                name = category.name
            ),
            price = price,
            currency = currency,
            status = when (status) {
                "ACTIVE" -> ServiceStatus.ACTIVE
                "PAUSED" -> ServiceStatus.PAUSED
                else -> ServiceStatus.ACTIVE
            },
            pendingRequestsCount = 0,
            imageUrl = imageUrl
        )
    }

    fun ReservationSummaryDto.toDomain(): ServiceRequest {
        return ServiceRequest(
            requestId = requestId,
            serviceTitle = serviceTitle,
            clientName = clientName,
            date = scheduledDate,
            timeRange = "${timeRange.start} - ${timeRange.end}",
            location = "" // No viene en la API, se puede dejar vacío o agregar después
        )
    }
}
