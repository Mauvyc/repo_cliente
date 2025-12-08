package com.example.serviconnecta.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationPreferences(
    private val dataStore: DataStore<Preferences>
) {

    private val locationIdKey = stringPreferencesKey("selected_location_id")
    private val locationLabelKey = stringPreferencesKey("selected_location_label")
    private val locationAddressKey = stringPreferencesKey("selected_location_address")
    private val locationLatitudeKey = doublePreferencesKey("selected_location_latitude")
    private val locationLongitudeKey = doublePreferencesKey("selected_location_longitude")
    private val locationIsDefaultKey = booleanPreferencesKey("selected_location_is_default")

    val selectedLocationIdFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[locationIdKey]
    }

    val selectedLocationLabelFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[locationLabelKey]
    }

    val selectedLocationAddressFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[locationAddressKey]
    }

    val selectedLocationLatitudeFlow: Flow<Double?> = dataStore.data.map { prefs ->
        prefs[locationLatitudeKey]
    }

    val selectedLocationLongitudeFlow: Flow<Double?> = dataStore.data.map { prefs ->
        prefs[locationLongitudeKey]
    }

    suspend fun saveSelectedLocation(
        id: String,
        label: String,
        address: String,
        latitude: Double?,
        longitude: Double?,
        isDefault: Boolean
    ) {
        dataStore.edit { prefs ->
            prefs[locationIdKey] = id
            prefs[locationLabelKey] = label
            prefs[locationAddressKey] = address
            latitude?.let { prefs[locationLatitudeKey] = it }
            longitude?.let { prefs[locationLongitudeKey] = it }
            prefs[locationIsDefaultKey] = isDefault
        }
    }

    suspend fun clearSelectedLocation() {
        dataStore.edit { prefs ->
            prefs.remove(locationIdKey)
            prefs.remove(locationLabelKey)
            prefs.remove(locationAddressKey)
            prefs.remove(locationLatitudeKey)
            prefs.remove(locationLongitudeKey)
            prefs.remove(locationIsDefaultKey)
        }
    }

    suspend fun getSelectedLocationId(): String? {
        var result: String? = null
        dataStore.data.map { prefs ->
            prefs[locationIdKey]
        }.collect { result = it }
        return result
    }

    suspend fun getSelectedLocationLabel(): String? {
        var result: String? = null
        dataStore.data.map { prefs ->
            prefs[locationLabelKey]
        }.collect { result = it }
        return result
    }

    suspend fun getSelectedLocationAddress(): String? {
        var result: String? = null
        dataStore.data.map { prefs ->
            prefs[locationAddressKey]
        }.collect { result = it }
        return result
    }
}
