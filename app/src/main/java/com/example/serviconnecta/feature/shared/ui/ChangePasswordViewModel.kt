package com.example.serviconnecta.feature.shared.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import com.example.serviconnecta.feature.shared.usecase.ChangePasswordUseCase

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                successMessage = null

                changePasswordUseCase(
                    currentPassword = currentPassword,
                    newPassword = newPassword
                )

                // Si llega aquí, el backend devolvió success = true
                successMessage = "Contraseña actualizada correctamente."

            } catch (e: HttpException) {
                errorMessage = when (e.code()) {
                    400 -> "Los datos enviados no son válidos."
                    401 -> "Tu sesión ha expirado o no estás autorizado."
                    403 -> "No tienes permisos para realizar esta acción."
                    500 -> "Error en el servidor. Intenta más tarde."
                    else -> "Error inesperado (${e.code()}). Intenta nuevamente."
                }
            } catch (e: IOException) {
                errorMessage = "Revisa tu conexión a internet."
            } catch (e: IllegalStateException) {
                // Por si tu repositorio lanza esta con el message del backend
                errorMessage = e.message ?: "No se pudo actualizar la contraseña."
            } catch (e: Exception) {
                errorMessage = "Ocurrió un error inesperado."
            } finally {
                isLoading = false
            }
        }
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }
}