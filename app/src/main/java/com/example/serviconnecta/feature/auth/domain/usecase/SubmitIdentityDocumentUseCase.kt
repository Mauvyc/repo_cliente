package com.example.serviconnecta.feature.auth.domain.usecase

import com.example.serviconnecta.feature.auth.data.IdentityVerificationRepository
import com.example.serviconnecta.feature.auth.domain.model.IdentityVerificationResult

class SubmitIdentityDocumentUseCase(
    private val repository: IdentityVerificationRepository
) {
    suspend operator fun invoke(
        country: String,
        documentType: String,
        imageBase64: String
    ): IdentityVerificationResult {
        return repository.submitDocument(country, documentType, imageBase64)
    }
}