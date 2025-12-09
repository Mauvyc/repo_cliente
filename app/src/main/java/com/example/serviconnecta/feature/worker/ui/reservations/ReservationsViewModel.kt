package com.example.serviconnecta.feature.worker.ui.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.worker.data.WorkerRepository
import com.example.serviconnecta.feature.worker.domain.model.ServiceRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReservationsUiState(
    val isLoading: Boolean = false,
    val reservations: List<ServiceRequest> = emptyList(),
    val selectedReservation: ServiceRequest? = null,
    val showDetailDialog: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class ReservationsViewModel(
    private val repository: WorkerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationsUiState())
    val uiState: StateFlow<ReservationsUiState> = _uiState.asStateFlow()

    init {
        loadReservations()
    }

    private fun loadReservations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Obtener las reservas confirmadas (status = ACCEPTED)
                val reservationsData = repository.getServiceRequests(
                    status = "ACCEPTED",
                    page = 1,
                    pageSize = 100
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        reservations = reservationsData.requests,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar reservas"
                    )
                }
            }
        }
    }

    fun showReservationDetail(reservation: ServiceRequest) {
        _uiState.update {
            it.copy(
                selectedReservation = reservation,
                showDetailDialog = true
            )
        }
    }

    fun hideReservationDetail() {
        _uiState.update {
            it.copy(
                selectedReservation = null,
                showDetailDialog = false
            )
        }
    }

    fun cancelReservation(requestId: String, reason: String = "Imprevisto de último minuto.") {
        viewModelScope.launch {
            try {
                repository.cancelReservation(requestId, reason)
                _uiState.update {
                    it.copy(
                        reservations = it.reservations.filter { req -> req.requestId != requestId },
                        successMessage = "Reserva cancelada exitosamente",
                        showDetailDialog = false,
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

    fun clearMessages() {
        _uiState.update {
            it.copy(errorMessage = null, successMessage = null)
        }
    }
}

class ReservationsViewModelFactory(
    private val repository: WorkerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
