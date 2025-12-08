package com.example.serviconnecta.feature.client.data.remote

import com.example.serviconnecta.core.network.StandardResponse
import retrofit2.http.GET
import retrofit2.http.Query

data class ClientHomeResponseDto(
    val user: HomeUserDto,
    val delivery_address: HomeDeliveryAddressDto?,
    val cart: HomeCartDto,
    val categories: List<HomeCategoryDto>,
    val top_services: List<HomeServiceDto>,
    val featured_workers: List<HomeWorkerDto>
)

data class HomeUserDto(
    val id: String,
    val full_name: String
)

data class HomeDeliveryAddressDto(
    val id: String,
    val label: String,
    val full_address: String,
    val latitude: Double,
    val longitude: Double
)

data class HomeCartDto(
    val items_count: Int
)

data class HomeCategoryDto(
    val id: String,
    val name: String,
    val icon_url: String?
)

data class HomeServiceDto(
    val id: String,
    val title: String,
    val category_id: String,
    val price: Double,
    val currency: String,
    val rating: Double,
    val reviews_count: Int,
    val image_url: String?,
    val provider: HomeServiceProviderDto
)

data class HomeServiceProviderDto(
    val id: String,
    val name: String,
    val profession: String,
    val avatar_url: String?
)

data class HomeWorkerDto(
    val id: String,
    val name: String,
    val profession: String,
    val avatar_url: String?,
    val rating: Double,
    val reviews_count: Int
)

data class ProviderDetailResponseDto(
    val provider: ProviderDetailDto,
    val services: List<ProviderServiceDto>
)

data class ProviderDetailDto(
    val id: String,
    val name: String,
    val profession: String,
    val avatar_url: String?,
    val rating: Double,
    val reviews_count: Int,
    val years_experience: Int?
)

data class ProviderServiceDto(
    val id: String,
    val title: String,
    val category_id: String,
    val price: Double,
    val currency: String,
    val rating: Double,
    val reviews_count: Int,
    val image_url: String?
)
