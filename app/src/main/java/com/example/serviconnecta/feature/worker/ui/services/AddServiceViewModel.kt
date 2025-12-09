package com.example.serviconnecta.feature.worker.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.worker.data.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddServiceUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "Servicio de electricidad",
    val categoryId: String = "692b8dc198d59291c777649e",
    val price: String = "",
    val imageBase64: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class AddServiceViewModel(
    private val repository: WorkerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddServiceUiState())
    val uiState: StateFlow<AddServiceUiState> = _uiState.asStateFlow()

    // Mapeo de nombres de categorías a IDs
    private val categoryMap = mapOf(
        "Servicio de electricidad" to "692b8dc198d59291c777649e",
        "Servicio de gasfitería" to "692b8dc198d59291c777649d",
        "Servicio de albañilería" to "692b8dc198d59291c777649f"
    )

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun updateCategory(category: String) {
        val categoryId = categoryMap[category] ?: "692b8dc198d59291c777649e"
        _uiState.update { it.copy(category = category, categoryId = categoryId) }
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

            try {
                repository.createService(
                    title = state.title,
                    description = state.description,
                    categoryId = state.categoryId,
                    price = state.price.toDouble(),
                    currency = "PEN",
                    imageBase64 = state.imageBase64
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al crear servicio"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

class AddServiceViewModelFactory(
    private val repository: WorkerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddServiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddServiceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
