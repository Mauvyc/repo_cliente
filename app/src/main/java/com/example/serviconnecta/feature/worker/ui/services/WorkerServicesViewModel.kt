package com.example.serviconnecta.feature.worker.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.worker.data.WorkerRepository
import com.example.serviconnecta.feature.worker.domain.model.Pagination
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
    val serviceRequests: List<ServiceRequest> = emptyList(),
    val requests: List<ServiceRequest> = emptyList(),
    val pagination: Pagination? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val selectedRequest: ServiceRequest? = null,
    val showRequestDetailDialog: Boolean = false,
    val selectedReservation: ServiceRequest? = null,
    val showReservationDetailDialog: Boolean = false
)

class WorkerServicesViewModel(
    private val repository: WorkerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerServicesUiState())
    val uiState: StateFlow<WorkerServicesUiState> = _uiState.asStateFlow()

    init {
        loadServices()
    }

    fun loadServices(page: Int = 1, pageSize: Int = 10, status: String = "ALL") {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Cargar servicios
                val servicesData = repository.getServices(page, pageSize, status)

                // Cargar solicitudes pendientes
                val serviceRequestsData = try {
                    repository.getServiceRequests(
                        status = "PENDING_PROVIDER_CONFIRMATION",
                        page = 1,
                        pageSize = 10
                    )
                } catch (e: Exception) {
                    null
                }

                // Cargar reservas confirmadas (ACCEPTED)
                val reservationsData = try {
                    repository.getServiceRequests(
                        status = "ACCEPTED",
                        page = 1,
                        pageSize = 10
                    )
                } catch (e: Exception) {
                    null
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        services = servicesData.services,
                        serviceRequests = serviceRequestsData?.requests ?: emptyList(),
                        requests = reservationsData?.requests ?: emptyList(),
                        pagination = servicesData.pagination,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar servicios"
                    )
                }
            }
        }
    }

    fun toggleServiceStatus(serviceId: String, currentStatus: String) {
        viewModelScope.launch {
            try {
                val newStatus = if (currentStatus == "ACTIVE") "PAUSED" else "ACTIVE"
                val updatedService = repository.updateService(
                    serviceId = serviceId,
                    status = newStatus
                )

                _uiState.update {
                    it.copy(
                        services = it.services.map { service ->
                            if (service.id == serviceId) updatedService else service
                        },
                        successMessage = "Estado actualizado"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error al actualizar estado")
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(errorMessage = null, successMessage = null)
        }
    }

    // Funciones para solicitudes
    fun showRequestDetail(request: ServiceRequest) {
        _uiState.update {
            it.copy(
                selectedRequest = request,
                showRequestDetailDialog = true
            )
        }
    }

    fun hideRequestDetail() {
        _uiState.update {
            it.copy(
                selectedRequest = null,
                showRequestDetailDialog = false
            )
        }
    }

    fun acceptRequest(requestId: String) {
        viewModelScope.launch {
            try {
                repository.acceptServiceRequest(requestId)
                _uiState.update {
                    it.copy(
                        serviceRequests = it.serviceRequests.filter { req -> req.requestId != requestId },
                        successMessage = "Solicitud aceptada exitosamente",
                        showRequestDetailDialog = false,
                        selectedRequest = null
                    )
                }
                loadServices() // Recargar para actualizar datos
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error al aceptar solicitud")
                }
            }
        }
    }

    fun rejectRequest(requestId: String) {
        viewModelScope.launch {
            try {
                repository.rejectServiceRequest(requestId)
                _uiState.update {
                    it.copy(
                        serviceRequests = it.serviceRequests.filter { req -> req.requestId != requestId },
                        successMessage = "Solicitud rechazada",
                        showRequestDetailDialog = false,
                        selectedRequest = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error al rechazar solicitud")
                }
            }
        }
    }

    // Funciones para reservas
    fun showReservationDetail(reservation: ServiceRequest) {
        _uiState.update {
            it.copy(
                selectedReservation = reservation,
                showReservationDetailDialog = true
            )
        }
    }

    fun hideReservationDetail() {
        _uiState.update {
            it.copy(
                selectedReservation = null,
                showReservationDetailDialog = false
            )
        }
    }

    fun cancelReservation(requestId: String, reason: String = "Imprevisto de último minuto.") {
        viewModelScope.launch {
            try {
                repository.cancelReservation(requestId, reason)
                _uiState.update {
                    it.copy(
                        requests = it.requests.filter { req -> req.requestId != requestId },
                        successMessage = "Reserva cancelada exitosamente",
                        showReservationDetailDialog = false,
                        selectedReservation = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error al cancelar reserva")
                }
            }
        }
    }
}

class WorkerServicesViewModelFactory(
    private val repository: WorkerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkerServicesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkerServicesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
