package com.example.serviconnecta.feature.worker.domain.model

data class Service(
    val id: String,
    val title: String,
    val description: String? = null,
    val category: ServiceCategory,
    val price: Double,
    val currency: String = "PEN",
    val status: ServiceStatus,
    val pendingRequestsCount: Int = 0,
    val imageUrl: String? = null
)

data class ServiceCategory(
    val id: String,
    val name: String
)

enum class ServiceStatus {
    ACTIVE, PAUSED
}

//data class ServiceRequest(
//    val id: String,
//    val serviceId: String,
//    val serviceName: String,
//    val clientId: String,
//    val clientName: String,
//    val clientPhoto: String?,
//    val date: String,
//    val time: String,
//    val location: String,
//    val status: RequestStatus,
//    val paymentMethod: String
//)

data class ServiceRequest(
    val requestId: String,
    val serviceTitle: String,
    val clientName: String,
    val date: String,
    val timeRange: String,
    val location: String
)

enum class RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    COMPLETED,
    CANCELLED
}

data class WorkerProfile(
    val id: String,
    val userId: String,
    val skills: List<String>,
    val experience: String,
    val portfolio: List<String>,
    val areas: List<String>,
    val rating: Double,
    val reviewCount: Int
)

data class Review(
    val id: String,
    val clientId: String,
    val clientName: String,
    val clientPhoto: String?,
    val serviceId: String,
    val serviceName: String,
    val rating: Int,
    val comment: String,
    val date: String
)

data class WorkerHome(
    val provider: Provider,
    val nextRequest: ServiceRequest?,
    val servicesSummary: ServicesSummary,
    val ratingSummary: RatingSummary
)

data class Provider(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val profession: String
)

data class ServicesSummary(
    val totalServices: Int,
    val activeServices: Int,
    val pausedServices: Int
)

data class RatingSummary(
    val averageRating: Double,
    val totalReviews: Int
)

data class TimeRange(
    val start: String,
    val end: String
)

data class NextReservation(
    val exists: Boolean,
    val requestId: String?,
    val serviceTitle: String?,
    val clientName: String?,
    val scheduledDate: String?,
    val timeRange: TimeRange?,
    val location: String?
)

data class WorkerHomeResponse(
    val provider: Provider,
    val nextReservation: NextReservation,
    val servicesSummary: ServicesSummary,
    val ratingSummary: RatingSummary
)

// Modelo de paginación
data class Pagination(
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int
)

// Modelo de respuesta completa de servicios
data class ServicesData(
    val pagination: Pagination,
    val services: List<Service>,
    val reservationsSummary: List<ServiceRequest>
)