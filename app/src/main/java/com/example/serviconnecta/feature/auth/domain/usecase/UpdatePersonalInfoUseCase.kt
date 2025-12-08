package com.example.serviconnecta.feature.auth.domain.usecase

import com.example.serviconnecta.feature.auth.data.UserProfileRepository

class UpdatePersonalInfoUseCase(
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke(
        fullName: String,
        gender: String,
        birthDate: String
    ) {
        userProfileRepository.updatePersonalInfo(fullName, gender, birthDate)
    }
}