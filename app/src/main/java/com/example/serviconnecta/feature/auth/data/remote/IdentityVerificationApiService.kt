package com.example.serviconnecta.feature.auth.data.remote

import com.example.serviconnecta.core.network.StandardResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class IdentityOptionsResponseDto(
    val country: String,
    val document_types: List<IdentityDocumentTypeDto>
)

data class IdentityDocumentTypeDto(
    val code: String,   // "NATIONAL_ID", "PASSPORT", "DRIVER_LICENSE"
    val label: String
)

data class SubmitIdentityDocumentRequestDto(
    val country: String,
    val document_type: String,
    val image_base64: String
)

data class SubmitIdentityDocumentResponseDto(
    val verification_id: String,
    val status: String   // "PENDING", "VERIFIED", etc.
)

data class IdentityStatusResponseDto(
    val user_id: String,
    val status: String,
    val checked_at: String?
)

interface IdentityVerificationApiService {

    @GET("verification/identity/options")
    suspend fun getOptions(
        @Query("country") country: String
    ): StandardResponse<IdentityOptionsResponseDto>

    @POST("verification/identity/document")
    suspend fun submitDocument(
        @Body body: SubmitIdentityDocumentRequestDto
    ): StandardResponse<SubmitIdentityDocumentResponseDto>

    @GET("verification/identity/status")
    suspend fun getStatus(
        @Query("user_id") userId: String
    ): StandardResponse<IdentityStatusResponseDto>
}
