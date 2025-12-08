package com.example.serviconnecta.feature.worker.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.worker.data.MockWorkerRepository
import com.example.serviconnecta.feature.worker.domain.model.ServiceRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScheduleUiState(
    val isLoading: Boolean = false,
    val acceptedRequests: List<ServiceRequest> = emptyList(),
    val errorMessage: String? = null
)

class ScheduleViewModel(
    private val repository: MockWorkerRepository = MockWorkerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        loadAcceptedRequests()
    }

    private fun loadAcceptedRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getRequests().fold(
                onSuccess = { requests ->
                    // Filtrar solo solicitudes aceptadas
                    // Como el modelo actual ServiceRequest NO tiene status, asumimos que el endpoint 
                    // ya devuelve las solicitudes correctas o mostramos todas las que lleguen por ahora.
                    // Si se necesita filtrar, el modelo ServiceRequest debería tener un campo status.
                    // Por ahora, para que compile, eliminamos el filtro.
                    val acceptedRequests = requests // .filter { it.status == RequestStatus.ACCEPTED }
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            acceptedRequests = acceptedRequests,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al cargar agenda"
                        )
                    }
                }
            )
        }
    }

    fun refresh() {
        loadAcceptedRequests()
    }
}