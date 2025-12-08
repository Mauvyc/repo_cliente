package com.example.serviconnecta.feature.worker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.worker.data.WorkerRepository
import com.example.serviconnecta.feature.worker.domain.model.NextReservation
import com.example.serviconnecta.feature.worker.domain.model.RatingSummary
import com.example.serviconnecta.feature.worker.domain.model.ServicesSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkerHomeUiState(
    val isLoading: Boolean = false,
    val nextReservation: NextReservation? = null,
    val servicesSummary: ServicesSummary? = null,
    val ratingSummary: RatingSummary? = null,
    val errorMessage: String? = null
)
class WorkerHomeViewModel(
    private val repository: WorkerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerHomeUiState())
    val uiState: StateFlow<WorkerHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val homeData = repository.getWorkerHomeData()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nextReservation = homeData.nextReservation,
                        servicesSummary = homeData.servicesSummary,
                        ratingSummary = homeData.ratingSummary,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar los datos"
                    )
                }
            }
        }
    }
}