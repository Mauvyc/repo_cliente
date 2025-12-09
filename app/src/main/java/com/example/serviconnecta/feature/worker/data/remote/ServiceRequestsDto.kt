package com.example.serviconnecta.feature.worker.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.example.serviconnecta.feature.worker.domain.model.ServiceRequest

// Request para listar solicitudes
@JsonClass(generateAdapter = true)
data class ServiceRequestsListRequest(
    @Json(name = "status") val status: String,
    @Json(name = "page") val page: Int,
    @Json(name = "page_size") val pageSize: Int
)

// Response de lista de solicitudes
@JsonClass(generateAdapter = true)
data class ServiceRequestsListResponse(
    @Json(name = "pagination") val pagination: PaginationDto,
    @Json(name = "requests") val requests: List<ServiceRequestItemDto>
)

@JsonClass(generateAdapter = true)
data class ServiceRequestItemDto(
    @Json(name = "request_id") val requestId: String,
    @Json(name = "client") val client: ClientInfoDto,
    @Json(name = "service") val service: ServiceInfoDto,
    @Json(name = "scheduled_date") val scheduledDate: String,
    @Json(name = "scheduled_time_range") val scheduledTimeRange: TimeRangeDto,
    @Json(name = "price_summary") val priceSummary: PriceSummaryDto?,
    @Json(name = "status") val status: String
)

@JsonClass(generateAdapter = true)
data class PriceSummaryDto(
    @Json(name = "currency") val currency: String,
    @Json(name = "total") val total: Double
)

// Response de detalle de solicitud
@JsonClass(generateAdapter = true)
data class ServiceRequestDetailResponse(
    @Json(name = "request_id") val requestId: String,
    @Json(name = "service") val service: ServiceInfoDto,
    @Json(name = "client") val client: ClientInfoDto,
    @Json(name = "location") val location: String,
    @Json(name = "scheduled_date") val scheduledDate: String,
    @Json(name = "time_range") val timeRange: TimeRangeDto,
    @Json(name = "payment_method") val paymentMethod: String,
    @Json(name = "status") val status: String
)

@JsonClass(generateAdapter = true)
data class ServiceInfoDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String
)

@JsonClass(generateAdapter = true)
data class ClientInfoDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "avatar_url") val avatarUrl: String?
)

// Request para aceptar solicitud
@JsonClass(generateAdapter = true)
data class AcceptRequestRequest(
    @Json(name = "notes") val notes: String?
)

// Response de aceptar solicitud
@JsonClass(generateAdapter = true)
data class AcceptRequestResponse(
    @Json(name = "request_id") val requestId: String,
    @Json(name = "status") val status: String
)

// Request para rechazar solicitud
@JsonClass(generateAdapter = true)
data class RejectRequestRequest(
    @Json(name = "reason") val reason: String?
)

// Response de rechazar solicitud
@JsonClass(generateAdapter = true)
data class RejectRequestResponse(
    @Json(name = "request_id") val requestId: String,
    @Json(name = "status") val status: String
)

// Request para cancelar reserva
@JsonClass(generateAdapter = true)
data class CancelReservationRequest(
    @Json(name = "reason") val reason: String?
)

// Response de cancelar reserva
@JsonClass(generateAdapter = true)
data class CancelReservationResponse(
    @Json(name = "request_id") val requestId: String,
    @Json(name = "status") val status: String
)

// Mappers
object ServiceRequestsMappers {
    fun ServiceRequestItemDto.toDomain(): ServiceRequest {
        return ServiceRequest(
            requestId = requestId,
            serviceTitle = service.title,
            clientName = client.name,
            date = scheduledDate,
            timeRange = "${scheduledTimeRange.start} - ${scheduledTimeRange.end}",
            location = "Por confirmar" // El backend no devuelve location en la lista
        )
    }

    fun ServiceRequestDetailResponse.toDomain(): ServiceRequest {
        return ServiceRequest(
            requestId = requestId,
            serviceTitle = service.title,
            clientName = client.name,
            date = scheduledDate,
            timeRange = "${timeRange.start} - ${timeRange.end}",
            location = location
        )
    }
}
