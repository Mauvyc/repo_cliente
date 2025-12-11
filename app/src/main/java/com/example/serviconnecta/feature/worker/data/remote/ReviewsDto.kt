package com.example.serviconnecta.feature.worker.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class MyReviewsResponseDto(
    @SerialName("summary") @Json(name = "summary") val summary: ReviewSummaryDto,
    @SerialName("pagination") @Json(name = "pagination") val pagination: ReviewPaginationDto,
    @SerialName("reviews") @Json(name = "reviews") val reviews: List<ReviewDto>
)

@Serializable
@JsonClass(generateAdapter = true)
data class ReviewSummaryDto(
    @SerialName("average_rating") @Json(name = "average_rating") val averageRating: Double,
    @SerialName("total_reviews") @Json(name = "total_reviews") val totalReviews: Int,
    @SerialName("distribution") @Json(name = "distribution") val distribution: Map<String, Int>
)

@Serializable
@JsonClass(generateAdapter = true)
data class ReviewPaginationDto(
    @SerialName("page") @Json(name = "page") val page: Int?,
    @SerialName("page_size") @Json(name = "page_size") val pageSize: Int?,
    @SerialName("total_items") @Json(name = "total_items") val totalItems: Int,
    @SerialName("total_pages") @Json(name = "total_pages") val totalPages: Int?
)

@Serializable
@JsonClass(generateAdapter = true)
data class ReviewDto(
    @SerialName("review_id") @Json(name = "review_id") val reviewId: String,
    @SerialName("created_at") @Json(name = "created_at") val createdAt: String,
    @SerialName("rating") @Json(name = "rating") val rating: Int,
    @SerialName("service") @Json(name = "service") val service: ReviewServiceDto,
    @SerialName("client") @Json(name = "client") val client: ReviewClientDto,
    @SerialName("comment") @Json(name = "comment") val comment: String
)

@Serializable
@JsonClass(generateAdapter = true)
data class ReviewServiceDto(
    @SerialName("id") @Json(name = "id") val id: String,
    @SerialName("title") @Json(name = "title") val title: String
)

@Serializable
@JsonClass(generateAdapter = true)
data class ReviewClientDto(
    @SerialName("id") @Json(name = "id") val id: String,
    @SerialName("name") @Json(name = "name") val name: String,
    @SerialName("avatar_url") @Json(name = "avatar_url") val avatarUrl: String?
)
