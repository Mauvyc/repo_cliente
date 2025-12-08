package com.example.serviconnecta.feature.auth.domain.usecase

import com.example.serviconnecta.feature.auth.data.AuthRepository

class ConfirmPhoneVerificationUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, code: String) {
        authRepository.confirmPhoneVerification(phoneNumber, code)
    }
}