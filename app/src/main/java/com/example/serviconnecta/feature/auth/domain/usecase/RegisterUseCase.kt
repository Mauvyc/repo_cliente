package com.example.serviconnecta.feature.auth.domain.usecase

import com.example.serviconnecta.feature.auth.data.AuthRepository
import com.example.serviconnecta.feature.auth.domain.model.AuthSession

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        accountType: String,
        acceptTerms: Boolean,
        acceptPrivacyPolicy: Boolean
    ): AuthSession {
        return authRepository.register(
            fullName,
            email,
            phoneNumber,
            password,
            accountType,
            acceptTerms,
            acceptPrivacyPolicy
        )
    }
}