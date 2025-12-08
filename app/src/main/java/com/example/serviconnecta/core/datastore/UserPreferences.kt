package com.example.serviconnecta.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    private val dataStore: DataStore<Preferences>
) {

    private val userIdKey = stringPreferencesKey("user_id")
    private val fullNameKey = stringPreferencesKey("full_name")
    private val emailKey = stringPreferencesKey("email")
    private val phoneNumberKey = stringPreferencesKey("phone_number")
    private val genderKey = stringPreferencesKey("gender")
    private val birthDateKey = stringPreferencesKey("birth_date")

    val userIdFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[userIdKey]
    }

    val fullNameFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[fullNameKey]
    }

    val emailFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[emailKey]
    }

    val phoneNumberFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[phoneNumberKey]
    }

    val genderFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[genderKey]
    }

    val birthDateFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[birthDateKey]
    }

    suspend fun saveUserData(
        userId: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        gender: String? = null,
        birthDate: String? = null
    ) {
        dataStore.edit { prefs ->
            prefs[userIdKey] = userId
            prefs[fullNameKey] = fullName
            prefs[emailKey] = email
            prefs[phoneNumberKey] = phoneNumber
            gender?.let { prefs[genderKey] = it }
            birthDate?.let { prefs[birthDateKey] = it }
        }
    }

    suspend fun updateUserData(
        fullName: String? = null,
        email: String? = null,
        phoneNumber: String? = null
    ) {
        dataStore.edit { prefs ->
            fullName?.let { prefs[fullNameKey] = it }
            email?.let { prefs[emailKey] = it }
            phoneNumber?.let { prefs[phoneNumberKey] = it }
        }
    }


    suspend fun clearUserData() {
        dataStore.edit { prefs ->
            prefs.remove(userIdKey)
            prefs.remove(fullNameKey)
            prefs.remove(emailKey)
            prefs.remove(phoneNumberKey)
            prefs.remove(genderKey)
            prefs.remove(birthDateKey)
        }
    }
}
