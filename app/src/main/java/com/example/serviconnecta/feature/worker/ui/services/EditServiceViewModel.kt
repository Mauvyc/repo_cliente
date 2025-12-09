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

data class EditServiceUiState(
    val serviceId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "Servicio de electricidad",
    val categoryId: String = "692b8dc198d59291c777649e",
    val price: String = "",
    val status: String = "ACTIVE",
    val imageBase64: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class EditServiceViewModel(
    private val repository: WorkerRepository,
    serviceId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditServiceUiState(serviceId = serviceId))
    val uiState: StateFlow<EditServiceUiState> = _uiState.asStateFlow()

    // Mapeo de nombres de categorías a IDs
    private val categoryMap = mapOf(
        "Servicio de electricidad" to "692b8dc198d59291c777649e",
        "Servicio de gasfitería" to "692b8dc198d59291c777649d",
        "Servicio de albañilería" to "692b8dc198d59291c777649f"
    )

    init {
        loadService()
    }

    private fun loadService() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Obtener todos los servicios y buscar el que necesitamos
                val servicesData = repository.getServices(page = 1, pageSize = 100, status = "ALL")
                val service = servicesData.services.find { it.id == _uiState.value.serviceId }

                if (service != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            title = service.title,
                            description = service.description ?: "",
                            category = service.category.name,
                            categoryId = service.category.id,
                            price = service.price.toString(),
                            status = service.status.name,
                            imageBase64 = service.imageUrl,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Servicio no encontrado"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar servicio"
                    )
                }
            }
        }
    }

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

    fun updateService() {
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
                repository.updateService(
                    serviceId = state.serviceId,
                    title = state.title,
                    description = state.description,
                    price = state.price.toDouble(),
                    status = state.status
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
                        errorMessage = e.message ?: "Error al actualizar servicio"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

class EditServiceViewModelFactory(
    private val repository: WorkerRepository,
    private val serviceId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditServiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditServiceViewModel(repository, serviceId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
