package com.example.serviconnecta.feature.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.auth.domain.model.AuthSession
import com.example.serviconnecta.feature.auth.domain.usecase.ConfirmPhoneVerificationUseCase
import com.example.serviconnecta.feature.auth.domain.usecase.RegisterUseCase
import com.example.serviconnecta.feature.auth.domain.usecase.RequestPhoneVerificationUseCase
import com.example.serviconnecta.feature.auth.domain.usecase.UpdatePersonalInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class AccountType {
    CONECTA_PRO,
    CLIENTE
}

enum class GenderType {
    MALE,
    FEMALE
}

data class RegistrationUiState(
    val accountType: AccountType? = null,
    val acceptedTerms: Boolean = false,
    val acceptedPrivacy: Boolean = false,
    val phoneNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val maskedPhone: String? = null,
    val verificationCode: String = "",
    val phoneVerified: Boolean = false,
    val fullName: String = "",
    val email: String = "",
    val gender: GenderType? = null,
    val birthDate: String = "",  // "YYYY-MM-DD"
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RegistrationViewModel(
    private val registerUseCase: RegisterUseCase,
    private val requestPhoneVerificationUseCase: RequestPhoneVerificationUseCase,
    private val confirmPhoneVerificationUseCase: ConfirmPhoneVerificationUseCase,
    private val updatePersonalInfoUseCase: UpdatePersonalInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState

    private var lastSession: AuthSession? = null

    fun resetState() {
        _uiState.value = RegistrationUiState()
        lastSession = null
    }

    fun setAccountType(type: AccountType) {
        _uiState.value = _uiState.value.copy(accountType = type)
    }

    fun setAcceptedTerms(value: Boolean) {
        _uiState.value = _uiState.value.copy(acceptedTerms = value)
    }

    fun setAcceptedPrivacy(value: Boolean) {
        _uiState.value = _uiState.value.copy(acceptedPrivacy = value)
    }

    fun setPhoneNumber(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, errorMessage = null)
    }

    fun setPassword(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun setConfirmPassword(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value, errorMessage = null)
    }

    fun setVerificationCode(value: String) {
        _uiState.value = _uiState.value.copy(verificationCode = value, errorMessage = null)
    }

    fun setPersonalInfo(fullName: String, email: String, gender: GenderType?, birthDate: String) {
        _uiState.value = _uiState.value.copy(
            fullName = fullName,
            email = email,
            gender = gender,
            birthDate = birthDate,
            errorMessage = null
        )
    }

    fun registerAndRequestCode(
        fullName: String? = null,
        email: String? = null,
        onSuccess: () -> Unit
    ) {
        // Actualizamos el estado con los parámetros si vienen (para compatibilidad con el UI que los pasa)
        if (fullName != null || email != null) {
            _uiState.value = _uiState.value.copy(
                fullName = fullName ?: _uiState.value.fullName,
                email = email ?: _uiState.value.email
            )
        }

        val state = _uiState.value

        if (state.accountType == null) {
            _uiState.value = state.copy(errorMessage = "Selecciona un tipo de cuenta.")
            return
        }

        if (state.phoneNumber.isBlank() ||
            state.password.isBlank() ||
            state.password != state.confirmPassword
        ) {
            _uiState.value = state.copy(errorMessage = "Verifica el número y las contraseñas.")
            return
        }

        val gender = state.gender ?: run {
            _uiState.value = state.copy(errorMessage = "Selecciona un género.")
            return
        }

        if (state.fullName.isBlank() || state.email.isBlank() || state.birthDate.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Completa todos los campos.")
            return
        }

        if (!state.acceptedTerms || !state.acceptedPrivacy) {
            _uiState.value = state.copy(errorMessage = "Debes aceptar los términos y la política de privacidad.")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true, errorMessage = null)

                val session = registerUseCase(
                    fullName = state.fullName,
                    email = state.email,
                    phoneNumber = state.phoneNumber,
                    password = state.password,
                    accountType = state.accountType.name,
                    acceptTerms = state.acceptedTerms,
                    acceptPrivacyPolicy = state.acceptedPrivacy
                )
                lastSession = session

                updatePersonalInfoUseCase(
                    fullName = state.fullName,
                    gender = gender.name,
                    birthDate = state.birthDate
                )

                requestPhoneVerificationUseCase(state.phoneNumber)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    maskedPhone = state.phoneNumber
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al registrar la cuenta"
                )
            }
        }
    }

    fun confirmVerification(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.phoneNumber.isBlank() || state.verificationCode.length < 4) {
            _uiState.value = state.copy(errorMessage = "Ingresa el código completo.")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true, errorMessage = null)
                confirmPhoneVerificationUseCase(state.phoneNumber, state.verificationCode)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    phoneVerified = true
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al verificar el teléfono"
                )
            }
        }
    }

    fun submitPersonalInfo(onSuccess: () -> Unit) {
        val state = _uiState.value
        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true, errorMessage = null)
                updatePersonalInfoUseCase(
                    fullName = state.fullName,
                    gender = state.gender?.name ?: "",
                    birthDate = state.birthDate
                )
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun updateRegistrationForm(
        fullName: String,
        email: String,
        gender: GenderType?,
        birthDate: String
    ) {
        _uiState.value = _uiState.value.copy(
            fullName = fullName,
            email = email,
            gender = gender,
            birthDate = birthDate,
            errorMessage = null
        )
    }

}
