package com.example.serviconnecta.feature.auth.data.remote

import com.example.serviconnecta.core.network.StandardResponse
import retrofit2.http.Body
import retrofit2.http.PATCH

data class UpdatePersonalInfoRequestDto(
    val full_name: String,
    val gender: String,      // "MALE" | "FEMALE"
    val birth_date: String   // "YYYY-MM-DD"
)

data class PersonalInfoResponseDto(
    val user_id: String,
    val full_name: String,
    val gender: String,
    val birth_date: String,
    val profile_completed: Boolean
)

interface UserApiService {

    @PATCH("users/me/personal-info")
    suspend fun updatePersonalInfo(
        @Body body: UpdatePersonalInfoRequestDto
    ): StandardResponse<PersonalInfoResponseDto>

    @PATCH("users/me")
    suspend fun updateUserProfile(
        @Body request: UpdateUserProfileRequestDto
    ): StandardResponse<UpdateUserProfileResponseDto>
}