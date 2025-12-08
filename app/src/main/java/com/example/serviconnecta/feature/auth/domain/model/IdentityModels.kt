package com.example.serviconnecta.feature.auth.domain.model

data class IdentityDocumentType(
    val code: String,
    val label: String
)

data class IdentityOptions(
    val country: String,
    val documentTypes: List<IdentityDocumentType>
)

data class IdentityVerificationResult(
    val verificationId: String,
    val status: String // p.ej. "VERIFIED"
)
