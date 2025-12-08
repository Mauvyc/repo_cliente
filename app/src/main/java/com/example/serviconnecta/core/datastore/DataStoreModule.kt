package com.example.serviconnecta.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// 👇 ESTA es la forma correcta para Preferences DataStore
val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "auth_prefs"
)

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

val Context.locationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "location_preferences"
)