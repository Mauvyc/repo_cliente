package com.example.serviconnecta.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.reviewedServicesDataStore: DataStore<Preferences> by preferencesDataStore(name = "reviewed_services")

/**
 * Gestiona el almacenamiento local de los IDs de servicios que ya fueron calificados.
 * Esto es una solución temporal mientras el backend implementa el campo has_review.
 */
class ReviewedServicesPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val REVIEWED_REQUEST_IDS = stringSetPreferencesKey("reviewed_request_ids")
    }

    /**
     * Obtiene el set de IDs de service requests que ya fueron calificados.
     */
    val reviewedRequestIds: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[REVIEWED_REQUEST_IDS] ?: emptySet()
    }

    /**
     * Marca un service request como calificado.
     */
    suspend fun markAsReviewed(requestId: String) {
        dataStore.edit { preferences ->
            val currentSet = preferences[REVIEWED_REQUEST_IDS]?.toMutableSet() ?: mutableSetOf()
            currentSet.add(requestId)
            preferences[REVIEWED_REQUEST_IDS] = currentSet
        }
    }

    /**
     * Verifica si un service request ya fue calificado.
     */
    suspend fun isReviewed(requestId: String): Boolean {
        val reviewedIds = reviewedRequestIds.first()
        return reviewedIds.contains(requestId)
    }

    /**
     * Limpia todos los IDs guardados (útil para logout).
     */
    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(REVIEWED_REQUEST_IDS)
        }
    }
}
