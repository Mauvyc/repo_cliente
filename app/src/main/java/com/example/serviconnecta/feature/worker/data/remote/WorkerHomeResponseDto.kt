package com.example.serviconnecta.feature.worker.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
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
@JsonClass(generateAdapter = true)
data class TimeRangeDto(
    @SerialName("start") @Json(name = "start") val start: String,
    @SerialName("end") @Json(name = "end") val end: String
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


@JsonClass(generateAdapter = true)
data class WorkerHomeResponseDto(
    @Json(name = "provider") val provider: ProviderDto,
    @Json(name = "next_request") val nextRequest: ServiceRequestDto?,
    @Json(name = "services_summary") val servicesSummary: ServicesSummaryDto,
    @Json(name = "rating_summary") val ratingSummary: RatingSummaryDto
)

@JsonClass(generateAdapter = true)
data class ProviderDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "profession") val profession: String
)

@JsonClass(generateAdapter = true)
data class ServiceRequestDto(
    @Json(name = "request_id") val requestId: String,
    @Json(name = "service_title") val serviceTitle: String,
    @Json(name = "client_name") val clientName: String,
    @Json(name = "date") val date: String,
    @Json(name = "time") val time: String,
    @Json(name = "location") val location: String
)

@JsonClass(generateAdapter = true)
data class ServicesSummaryDto(
    @Json(name = "total_services") val totalServices: Int,
    @Json(name = "active_services") val activeServices: Int,
    @Json(name = "paused_services") val pausedServices: Int
)

@JsonClass(generateAdapter = true)
data class RatingSummaryDto(
    @Json(name = "average_rating") val averageRating: Double,
    @Json(name = "total_reviews") val totalReviews: Int
)
