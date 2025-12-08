package com.example.serviconnecta.feature.worker.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class WorkerHomeResponseDto(
//    val provider: ProviderDto,
//    @SerialName("next_reservation")
//    val nextReservation: NextReservationDto?,
//    @SerialName("services_summary")
//    val servicesSummary: ServicesSummaryDto,
//    @SerialName("rating_summary")
//    val ratingSummary: RatingSummaryDto
//)

//@Serializable
//data class ProviderDto(
//    val id: String,
//    val name: String,
//    @SerialName("avatar_url")
//    val avatarUrl: String?,
//    val profession: String
//)

@Serializable
data class NextReservationDto(
    val exists: Boolean,
    @SerialName("request_id")
    val requestId: String?,
    @SerialName("service_title")
    val serviceTitle: String?,
    @SerialName("client_name")
    val clientName: String?,
    @SerialName("scheduled_date")
    val scheduledDate: String?,
    @SerialName("time_range")
    val timeRange: TimeRangeDto,
    val location: String?
)

@Serializable
data class TimeRangeDto(
    val start: String,
    val end: String
)

//@Serializable
//data class ServicesSummaryDto(
//    @SerialName("total_services")
//    val totalServices: Int,
//    @SerialName("active_services")
//    val activeServices: Int,
//    @SerialName("paused_services")
//    val pausedServices: Int
//)

//@Serializable
//data class RatingSummaryDto(
//    @SerialName("average_rating")
//    val averageRating: Double,
//    @SerialName("total_reviews")
//    val totalReviews: Int
//)


data class WorkerHomeResponseDto(
    val provider: ProviderDto,
    val nextRequest: ServiceRequestDto?,
    val servicesSummary: ServicesSummaryDto,
    val ratingSummary: RatingSummaryDto
)

data class ProviderDto(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val profession: String
)

data class ServiceRequestDto(
    val requestId: String,
    val serviceTitle: String,
    val clientName: String,
    val date: String,
    val time: String,
    val location: String
)

data class ServicesSummaryDto(
    val totalServices: Int,
    val activeServices: Int,
    val pausedServices: Int
)

data class RatingSummaryDto(
    val averageRating: Double,
    val totalReviews: Int
)
