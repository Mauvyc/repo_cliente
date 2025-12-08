package com.example.serviconnecta.feature.shared.ui

import com.example.serviconnecta.feature.shared.usecase.UpdateUserProfileUseCase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.core.datastore.UserPreferences
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var updateSuccess by mutableStateOf(false)
        private set

    fun saveProfile(
        fullName: String,
        email: String,
        phone: String
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                updateSuccess = false

                // 1) Llamar al backend
                updateUserProfileUseCase(
                    fullName = fullName,
                    email = email,
                    phoneNumber = phone,
                    avatarUrl = null
                )

                // 2) Si todo OK, actualizar DataStore local
                userPreferences.updateUserData(
                    fullName,
                    email,
                    phone
                )

                // 3) Marcar éxito
                updateSuccess = true

            } catch (e: Exception) {
                errorMessage = e.message ?: "Error al actualizar perfil"
            } finally {
                isLoading = false
            }
        }
    }

    fun consumeSuccess() {
        updateSuccess = false
    }
}