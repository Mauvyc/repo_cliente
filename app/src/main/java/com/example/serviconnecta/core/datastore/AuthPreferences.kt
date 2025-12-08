package com.example.serviconnecta.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreferences(
    private val dataStore: DataStore<Preferences>
) {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val accountTypeKey = stringPreferencesKey("account_type")

    val accessTokenFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[accessTokenKey]
    }

    val refreshTokenFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[refreshTokenKey]
    }

    val accountTypeFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[accountTypeKey]
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[accessTokenKey] = accessToken
            prefs[refreshTokenKey] = refreshToken
        }
    }

    suspend fun saveAccountType(accountType: String) {
        dataStore.edit { prefs ->
            prefs[accountTypeKey] = accountType
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(accessTokenKey)
            prefs.remove(refreshTokenKey)
            prefs.remove(accountTypeKey)
        }
    }
}