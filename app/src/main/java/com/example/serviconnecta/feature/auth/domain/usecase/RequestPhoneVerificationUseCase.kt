package com.example.serviconnecta.feature.auth.domain.usecase

import com.example.serviconnecta.feature.auth.data.AuthRepository

class RequestPhoneVerificationUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String) {
        authRepository.requestPhoneVerification(phoneNumber)
    }
}