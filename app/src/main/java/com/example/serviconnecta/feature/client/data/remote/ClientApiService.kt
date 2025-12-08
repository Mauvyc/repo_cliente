package com.example.serviconnecta.feature.client.data.remote

import com.example.serviconnecta.core.network.StandardResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ClientApiService {

    @GET("client/home")
    suspend fun getClientHome(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): StandardResponse<ClientHomeResponseDto>

    @GET("categories/{category_id}/services")
    suspend fun getServicesByCategory(
        @Path("category_id") categoryId: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
        @Query("search") search: String = ""
    ): StandardResponse<CategoryServicesResponseDto>

    @GET("services/services/{service_id}")
    suspend fun getServiceDetail(
        @Path("service_id") serviceId: String
    ): StandardResponse<ServiceDetailDto>

    @GET("client/service-requests")
    suspend fun getServiceRequests(
        @Query("status") status: String = "ALL",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10
    ): StandardResponse<ClientReservationsResponseDto>

    @GET("client/locations")
    suspend fun getClientLocations(): StandardResponse<ClientLocationsResponseDto>

    @POST("client/locations")
    suspend fun createLocation(
        @Body body: CreateLocationRequestDto
    ): StandardResponse<LocationDto>

    @GET("client/payment-methods")
    suspend fun getPaymentMethods(): StandardResponse<PaymentMethodsResponseDto>

    @POST("client/payment-methods")
    suspend fun createPaymentMethod(
        @Body body: CreatePaymentMethodRequestDto
    ): StandardResponse<PaymentMethodDto>

    @POST("client/service-request")
    suspend fun createServiceRequest(
        @Body body: CreateServiceRequestDto
    ): StandardResponse<ServiceRequestCreatedDto>

    @GET("services/search")
    suspend fun searchServices(
        @Query("q") query: String,
        @Query("category") categoryId: String? = null,
        @Query("location") location: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): StandardResponse<SearchServicesResponseDto>

    @POST("client/reviews")
    suspend fun submitReview(
        @Body body: SubmitReviewRequestDto
    ): StandardResponse<SubmitReviewResponseDto>

    @GET("services/providers/{provider_id}")
    suspend fun getProviderDetail(
        @Path("provider_id") providerId: String
    ): StandardResponse<ProviderDetailResponseDto>
}
