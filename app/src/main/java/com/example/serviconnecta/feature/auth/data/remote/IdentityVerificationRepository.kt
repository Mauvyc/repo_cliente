package com.example.serviconnecta.feature.auth.data

import com.example.serviconnecta.feature.auth.data.remote.IdentityVerificationApiService
import com.example.serviconnecta.feature.auth.data.remote.SubmitIdentityDocumentRequestDto
import com.example.serviconnecta.feature.auth.domain.model.IdentityDocumentType
import com.example.serviconnecta.feature.auth.domain.model.IdentityOptions
import com.example.serviconnecta.feature.auth.domain.model.IdentityVerificationResult

class IdentityVerificationRepository(
    private val api: IdentityVerificationApiService
) {

    suspend fun getOptions(country: String): IdentityOptions {
        val response = api.getOptions(country)

        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message)
        }

        val dto = response.data

        return IdentityOptions(
            country = dto.country,
            documentTypes = dto.document_types.map {
                IdentityDocumentType(code = it.code, label = it.label)
            }
        )
    }

    suspend fun submitDocument(
        country: String,
        documentType: String,
        imageBase64: String
    ): IdentityVerificationResult {
        val request = SubmitIdentityDocumentRequestDto(
            country = country,
            document_type = documentType,
            image_base64 = imageBase64
        )

        val response = api.submitDocument(request)

        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message)
        }

        val dto = response.data

        return IdentityVerificationResult(
            verificationId = dto.verification_id,
            status = dto.status
        )
    }
}
