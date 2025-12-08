package com.example.serviconnecta.feature.shared.usecase

import com.example.serviconnecta.feature.auth.data.AuthRepositoryImpl

class ChangePasswordUseCase(
    private val authRepository: AuthRepositoryImpl
) {
    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String
    ) {
        authRepository.changePassword(currentPassword, newPassword)
    }
}