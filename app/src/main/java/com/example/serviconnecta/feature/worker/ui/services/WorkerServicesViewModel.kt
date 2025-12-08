package com.example.serviconnecta.feature.worker.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.worker.data.MockWorkerRepository
import com.example.serviconnecta.feature.worker.domain.model.Service
import com.example.serviconnecta.feature.worker.domain.model.ServiceRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkerServicesUiState(
    val isLoading: Boolean = false,
    val services: List<Service> = emptyList(),
    val requests: List<ServiceRequest> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class WorkerServicesViewModel(
    private val repository: MockWorkerRepository = MockWorkerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerServicesUiState())
    val uiState: StateFlow<WorkerServicesUiState> = _uiState.asStateFlow()

    init {
        loadServices()
        loadRequests()
    }

    private fun loadServices() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getMyServices().fold(
                onSuccess = { services ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            services = services,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al cargar servicios"
                        )
                    }
                }
            )
        }
    }

    private fun loadRequests() {
        viewModelScope.launch {
            repository.getRequests().fold(
                onSuccess = { requests ->
                    _uiState.update {
                        it.copy(requests = requests)
                    }
                },
                onFailure = { }
            )
        }
    }

    fun deleteService(serviceId: String) {
        viewModelScope.launch {
            repository.deleteService(serviceId).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            successMessage = "Servicio eliminado",
                            services = it.services.filter { service -> service.id != serviceId }
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message ?: "Error al eliminar")
                    }
                }
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(errorMessage = null, successMessage = null)
        }
    }
}
