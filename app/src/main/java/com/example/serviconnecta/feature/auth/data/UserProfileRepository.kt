package com.example.serviconnecta.feature.auth.data

import com.example.serviconnecta.feature.auth.data.remote.UpdatePersonalInfoRequestDto
import com.example.serviconnecta.feature.auth.data.remote.UpdateUserProfileRequestDto
import com.example.serviconnecta.feature.auth.data.remote.UserApiService

class UserProfileRepository(
    private val userApiService: UserApiService
) {
    suspend fun updatePersonalInfo(
        fullName: String,
        gender: String,
        birthDate: String
    ) {
        val request = UpdatePersonalInfoRequestDto(
            full_name = fullName,
            gender = gender,
            birth_date = birthDate
        )

        val response = userApiService.updatePersonalInfo(request)
        if (!response.success) {
            throw IllegalStateException(response.message)
        }
    }

    suspend fun updateUserProfile(
        fullName: String?,
        email: String?,
        phoneNumber: String?,
        avatarUrl: String?
    ) {
        val request = UpdateUserProfileRequestDto(
            full_name = fullName,
            email = email,
            phone_number = phoneNumber,
            avatar_url = avatarUrl
        )

        val response = userApiService.updateUserProfile(request)
        if (!response.success) {
            throw IllegalStateException(response.message)
        }
        // response.data?.user tiene el user actualizado
    }
}