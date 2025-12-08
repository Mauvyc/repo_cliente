package com.example.serviconnecta.core.session

import com.example.serviconnecta.core.datastore.AuthPreferences
import com.example.serviconnecta.core.datastore.UserPreferences

class SessionManager(
    private val authPreferences: AuthPreferences,
    private val userPreferences: UserPreferences
) {

    suspend fun clearSession() {
        // Limpia tokens + tipo de cuenta
        authPreferences.clearTokens()

        // si tienes algo así, limpias datos de usuario
        userPreferences.clearUserData()
    }
}