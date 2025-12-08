package com.example.serviconnecta.feature.auth.data.remote

import com.example.serviconnecta.core.network.StandardResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): StandardResponse<LoginResponseDto>

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequestDto
    ): StandardResponse<RegisterResponseDto>

    @POST("auth/verify-phone/request")
    suspend fun requestPhoneVerification(
        @Body body: VerifyPhoneRequestDto
    ): StandardResponse<VerifyPhoneRequestResponseDto>

    @POST("auth/verify-phone/confirm")
    suspend fun confirmPhoneVerification(
        @Body body: VerifyPhoneConfirmDto
    ): StandardResponse<VerifyPhoneConfirmResponseDto>

    @POST("auth/change-password")
    suspend fun changePassword(
        @Body body: ChangePasswordRequestDto
    ): StandardResponse<ChangePasswordResponseDto>
}
