package com.example.serviconnecta.feature.worker.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.worker.data.MockWorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddServiceUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "Servicio de electricidad",
    val price: String = "",
    val imageBase64: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class AddServiceViewModel(
    private val repository: MockWorkerRepository = MockWorkerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddServiceUiState())
    val uiState: StateFlow<AddServiceUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun updateCategory(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun updatePrice(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    fun updateImage(imageBase64: String?) {
        _uiState.update { it.copy(imageBase64 = imageBase64) }
    }

    fun createService() {
        val state = _uiState.value

        if (state.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El título es requerido") }
            return
        }

        if (state.description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "La descripción es requerida") }
            return
        }

        if (state.price.isBlank() || state.price.toDoubleOrNull() == null) {
            _uiState.update { it.copy(errorMessage = "El precio debe ser un número válido") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.createService(
                title = state.title,
                description = state.description,
                category = state.category,
                price = state.price.toDouble(),
                imageBase64 = state.imageBase64
            ).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al crear servicio"
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
