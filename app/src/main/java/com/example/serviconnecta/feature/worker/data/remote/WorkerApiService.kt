package com.example.serviconnecta.feature.worker.data.remote

import com.example.serviconnecta.core.network.StandardResponse
import com.example.serviconnecta.feature.worker.domain.model.WorkerHomeResponse
import retrofit2.http.*

interface WorkerApiService {

    @GET("/provider/home")
    suspend fun getHomeData(): StandardResponse<WorkerHomeResponse>

    @GET("/provider/services")
    suspend fun getServices(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
        @Query("status") status: String = "ALL"
    ): StandardResponse<ServicesResponse>

    @POST("/provider/services")
    suspend fun createService(@Body request: CreateServiceRequest): StandardResponse<CreateServiceResponse>

    @PATCH("/provider/services/{service_id}")
    suspend fun updateService(
        @Path("service_id") serviceId: String,
        @Body request: UpdateServiceRequest
    ): StandardResponse<CreateServiceResponse>

    @GET("/provider/service-requests")
    suspend fun getServiceRequests(
        @Query("status") status: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10
    ): StandardResponse<ServiceRequestsListResponse>

    @GET("/provider/service-request/{request_id}")
    suspend fun getServiceRequestDetail(
        @Path("request_id") requestId: String
    ): StandardResponse<ServiceRequestDetailResponse>

    @POST("/provider/service-requests/{request_id}/accept")
    suspend fun acceptServiceRequest(
        @Path("request_id") requestId: String,
        @Body request: AcceptRequestRequest
    ): StandardResponse<AcceptRequestResponse>

    @POST("/provider/service-requests/{request_id}/reject")
    suspend fun rejectServiceRequest(
        @Path("request_id") requestId: String,
        @Body request: RejectRequestRequest
    ): StandardResponse<RejectRequestResponse>

    @POST("/provider/service-request/{request_id}/cancel")
    suspend fun cancelReservation(
        @Path("request_id") requestId: String,
        @Body request: CancelReservationRequest
    ): StandardResponse<CancelReservationResponse>
}