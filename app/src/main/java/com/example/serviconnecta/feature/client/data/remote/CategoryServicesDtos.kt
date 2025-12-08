package com.example.serviconnecta.feature.client.data.remote

import com.example.serviconnecta.core.network.StandardResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class CategoryServicesResponseDto(
    val category: CategoryHeaderDto,
    val pagination: PaginationDto,
    val services: List<ServiceDto>
)

data class CategoryHeaderDto(
    val id: String,
    val name: String
)

data class PaginationDto(
    val page: Int,
    val page_size: Int,
    val total_items: Int,
    val total_pages: Int
)

data class ServiceDto(
    val id: String,
    val title: String,
    val price: Double,
    val currency: String,
    val rating: Double,
    val reviews_count: Int,
    val image_url: String?,
    val provider: ServiceProviderDto
)

data class ServiceProviderDto(
    val id: String,
    val name: String,
    val profession: String,
    val avatar_url: String?
)

data class ServiceDetailDto(
    val id: String,
    val title: String,
    val description: String,
    val category: ServiceDetailCategoryDto,
    val price: Double,
    val currency: String,
    val rating: Double,
    val reviews_count: Int,
    val image_url: String?,
    val provider: ServiceDetailProviderDto,
    val comments: List<ServiceDetailCommentDto>
)

data class ServiceDetailCategoryDto(
    val id: String,
    val name: String
)

data class ServiceDetailProviderDto(
    val id: String,
    val name: String,
    val profession: String,
    val avatar_url: String?,
    val years_experience: Int
)

data class ServiceDetailCommentDto(
    val id: String,
    val author_name: String,
    val rating: Double,
    val comment: String,
    val created_at: String
)