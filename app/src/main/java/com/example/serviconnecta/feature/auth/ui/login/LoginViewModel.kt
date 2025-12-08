package com.example.serviconnecta.feature.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class LoginUiState(
    val phoneNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val accountType: String? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onPhoneNumberChanged(newPhoneNumber: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = newPhoneNumber,
            errorMessage = null
        )
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.value = _uiState.value.copy(
            password = newPassword,
            errorMessage = null
        )
    }

    fun onLoginClicked() {
        val state = _uiState.value

        if (state.phoneNumber.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(
                errorMessage = "Ingresa tu número de teléfono y contraseña."
            )
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(
                    isLoading = true,
                    errorMessage = null,
                    loginSuccess = false
                )

                // ⬇️ Ahora recibimos toda la sesión (user + tokens + status)
                val authSession = loginUseCase(
                    phoneNumber = state.phoneNumber,
                    password = state.password
                )

                val status = authSession.user.status

                // ✅ Validar flags de verificación del backend
                if (!status.phoneVerified ||
                    !status.identityVerified ||
                    !status.profileCompleted
                ) {
                    val message = when {
                        !status.phoneVerified ->
                            "Debes verificar tu número de teléfono antes de continuar."
                        !status.identityVerified ->
                            "Debes completar la verificación de identidad antes de continuar."
                        !status.profileCompleted ->
                            "Debes completar tu perfil antes de continuar."
                        else ->
                            "Tu cuenta aún no está lista para iniciar sesión."
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loginSuccess = false,
                        errorMessage = message
                    )
                    return@launch
                }

                // Si todo está en true → login exitoso
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                    accountType = authSession.user.accountType
                )

            } catch (e: Exception) {
                val userFriendlyMessage = when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            400, 401 -> "Credenciales inválidas. Verifica tu número y contraseña."
                            403 -> "No tienes permisos para acceder. Revisa tus datos."
                            500, 502, 503 -> "Tenemos un problema en el servidor. Intenta nuevamente en unos minutos."
                            else -> "Ocurrió un error (${e.code()}). Intenta nuevamente."
                        }
                    }
                    is IOException -> {
                        "Revisa tu conexión a internet e inténtalo nuevamente."
                    }
                    else -> {
                        "Ocurrió un error inesperado. Intenta nuevamente."
                    }
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = false,
                    errorMessage = userFriendlyMessage
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}
