package com.example.serviconnecta.feature.auth.domain.usecase

import com.example.serviconnecta.feature.auth.data.IdentityVerificationRepository
import com.example.serviconnecta.feature.auth.domain.model.IdentityOptions

class GetIdentityOptionsUseCase(
    private val repository: IdentityVerificationRepository
) {
    suspend operator fun invoke(country: String): IdentityOptions {
        return repository.getOptions(country)
    }
}