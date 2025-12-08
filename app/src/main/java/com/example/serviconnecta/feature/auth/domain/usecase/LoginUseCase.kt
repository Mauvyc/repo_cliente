package com.example.serviconnecta.feature.auth.domain.usecase

import com.example.serviconnecta.feature.auth.data.AuthRepository
import com.example.serviconnecta.feature.auth.domain.model.AuthSession

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        password: String
    ): AuthSession {
        return authRepository.login(
            phoneNumber = phoneNumber,
            password = password
        )
    }
}