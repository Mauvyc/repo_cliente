package com.example.serviconnecta.feature.auth.data

import com.example.serviconnecta.feature.auth.domain.model.AuthSession

interface AuthRepository {

    suspend fun login(phoneNumber: String, password: String): AuthSession

    suspend fun register(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        accountType: String,
        acceptTerms: Boolean,
        acceptPrivacyPolicy: Boolean
    ): AuthSession

    suspend fun requestPhoneVerification(phoneNumber: String)

    suspend fun confirmPhoneVerification(phoneNumber: String, code: String)

    interface AuthRepository {
        suspend fun changePassword(currentPassword: String, newPassword: String)
    }
}