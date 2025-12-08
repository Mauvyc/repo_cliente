package com.example.serviconnecta.feature.auth.data

import com.example.serviconnecta.core.datastore.AuthPreferences
import com.example.serviconnecta.core.network.StandardResponse
import com.example.serviconnecta.feature.auth.data.remote.AuthApiService
import com.example.serviconnecta.feature.auth.data.remote.ChangePasswordRequestDto
import com.example.serviconnecta.feature.auth.data.remote.ChangePasswordResponseDto
import com.example.serviconnecta.feature.auth.data.remote.LoginRequestDto
import com.example.serviconnecta.feature.auth.data.remote.RegisterRequestDto
import com.example.serviconnecta.feature.auth.data.remote.VerifyPhoneConfirmDto
import com.example.serviconnecta.feature.auth.data.remote.VerifyPhoneRequestDto
import com.example.serviconnecta.feature.auth.domain.model.AuthSession
import com.example.serviconnecta.feature.auth.domain.model.AuthTokens
import com.example.serviconnecta.feature.auth.domain.model.User
import com.example.serviconnecta.feature.auth.domain.model.UserStatus

class AuthRepositoryImpl(
    private val api: AuthApiService,
    private val authPreferences: AuthPreferences,
    private val userPreferences: com.example.serviconnecta.core.datastore.UserPreferences
) : AuthRepository {

    override suspend fun login(phoneNumber: String, password: String): AuthSession {
        val response = api.login(LoginRequestDto(phoneNumber, password))

        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message)
        }

        val dto = response.data

        val user = User(
            id = dto.user.id,
            fullName = dto.user.fullName,
            phoneNumber = dto.user.phoneNumber,
            email = dto.user.email,
            accountType = dto.user.accountType,
            status = UserStatus(
                phoneVerified = dto.user.status.phoneVerified,
                identityVerified = dto.user.status.identityVerified,
                profileCompleted = dto.user.status.profileCompleted
            )
        )

        val tokens = AuthTokens(
            accessToken = dto.tokens.accessToken,
            refreshToken = dto.tokens.refreshToken,
            expiresIn = dto.tokens.expiresIn
        )

        authPreferences.saveTokens(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken
        )

        authPreferences.saveAccountType(user.accountType)

        userPreferences.saveUserData(
            userId = user.id,
            fullName = user.fullName,
            email = user.email,
            phoneNumber = user.phoneNumber
        )

        return AuthSession(user = user, tokens = tokens)
    }

    override suspend fun register(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        accountType: String,
        acceptTerms: Boolean,
        acceptPrivacyPolicy: Boolean
    ): AuthSession {
        val request = RegisterRequestDto(
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber,
            password = password,
            accountType = accountType,
            acceptTerms = acceptTerms,
            acceptPrivacyPolicy = acceptPrivacyPolicy
        )

        val response = api.register(request)

        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message)
        }

        val dto = response.data

        // La respuesta de register ya trae los datos del usuario "flat"
        val user = User(
            id = dto.userId,
            fullName = dto.fullName,
            phoneNumber = dto.phoneNumber,
            email = dto.email,
            accountType = dto.accountType,
            status = UserStatus(
                phoneVerified = dto.status.phoneVerified,
                identityVerified = dto.status.identityVerified,
                profileCompleted = dto.status.profileCompleted
            )
        )

        val tokens = AuthTokens(
            accessToken = dto.tokens.accessToken,
            refreshToken = dto.tokens.refreshToken,
            expiresIn = dto.tokens.expiresIn
        )

        authPreferences.saveTokens(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken
        )

        authPreferences.saveAccountType(user.accountType)

        userPreferences.saveUserData(
            userId = user.id,
            fullName = user.fullName,
            email = user.email,
            phoneNumber = user.phoneNumber
        )

        return AuthSession(user = user, tokens = tokens)
    }

    override suspend fun requestPhoneVerification(phoneNumber: String) {
        val response = api.requestPhoneVerification(
            VerifyPhoneRequestDto(phoneNumber)
        )
        if (!response.success) {
            throw IllegalStateException(response.message)
        }
    }

    override suspend fun confirmPhoneVerification(phoneNumber: String, code: String) {
        val response = api.confirmPhoneVerification(
            VerifyPhoneConfirmDto(phoneNumber, code)
        )
        if (!response.success) {
            throw IllegalStateException(response.message)
        }
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ) {
        val request = ChangePasswordRequestDto(
            current_password = currentPassword,
            new_password = newPassword
        )

        val response: StandardResponse<ChangePasswordResponseDto> =
            api.changePassword(request)

        if (!response.success) {
            throw IllegalStateException(
                response.message.ifBlank { "No se pudo actualizar la contraseña" }
            )
        }
    }
}
