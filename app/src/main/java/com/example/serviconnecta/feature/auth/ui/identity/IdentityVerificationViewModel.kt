package com.example.serviconnecta.feature.auth.ui.identity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.auth.domain.model.IdentityDocumentType
import com.example.serviconnecta.feature.auth.domain.usecase.GetIdentityOptionsUseCase
import com.example.serviconnecta.feature.auth.domain.usecase.SubmitIdentityDocumentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class IdentityUiState(
    val country: String = "PE",
    val documentTypes: List<IdentityDocumentType> = emptyList(),
    val selectedDocumentTypeCode: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isVerified: Boolean = false
)

class IdentityVerificationViewModel(
    private val getIdentityOptionsUseCase: GetIdentityOptionsUseCase,
    private val submitIdentityDocumentUseCase: SubmitIdentityDocumentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(IdentityUiState())
    val uiState: StateFlow<IdentityUiState> = _uiState

    fun loadOptions() {
        val country = _uiState.value.country
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                val options = getIdentityOptionsUseCase(country)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    documentTypes = options.documentTypes,
                    selectedDocumentTypeCode = options.documentTypes.firstOrNull()?.code
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al obtener opciones de identidad"
                )
            }
        }
    }

    fun selectDocumentType(code: String) {
        _uiState.value = _uiState.value.copy(selectedDocumentTypeCode = code)
    }

    fun submitDocument(
        imageBase64: String,
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value
        val documentType = state.selectedDocumentTypeCode ?: run {
            _uiState.value = state.copy(errorMessage = "Selecciona un tipo de documento.")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true, errorMessage = null)

                val result = submitIdentityDocumentUseCase(
                    country = state.country,
                    documentType = documentType,
                    imageBase64 = imageBase64
                )

                val verified = result.status == "VERIFIED"
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isVerified = verified
                )

                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al enviar documento"
                )
            }
        }
    }

}