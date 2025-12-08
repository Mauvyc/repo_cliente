package com.example.serviconnecta.feature.shared.usecase

import com.example.serviconnecta.feature.auth.data.UserProfileRepository

class UpdateUserProfileUseCase(
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke(
        fullName: String?,
        email: String?,
        phoneNumber: String?,
        avatarUrl: String?
    ) {
        userProfileRepository.updateUserProfile(
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber,
            avatarUrl = avatarUrl
        )
    }
}