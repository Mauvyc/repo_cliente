package com.example.serviconnecta.feature.client.ui.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClientLocationsUiState(
    val isLoading: Boolean = false,
    val locations: List<Location> = emptyList(),
    val errorMessage: String? = null,
    val showAddDialog: Boolean = false
)

class ClientLocationsViewModel(
    private val clientServicesRepository: ClientServicesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientLocationsUiState())
    val uiState: StateFlow<ClientLocationsUiState> = _uiState.asStateFlow()

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val locations = clientServicesRepository.getClientLocations()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        locations = locations,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar ubicaciones"
                    )
                }
            }
        }
    }

    fun addLocation(label: String, address: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // usamos la versión que mete lat/long dummy
                clientServicesRepository.createLocation(label, address)
                _uiState.update { it.copy(showAddDialog = false) }
                loadLocations()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al agregar ubicación"
                    )
                }
            }
        }
    }

    fun showAddDialog() {
        _uiState.update { it.copy(showAddDialog = true) }
    }

    fun hideAddDialog() {
        _uiState.update { it.copy(showAddDialog = false) }
    }

    fun refresh() {
        loadLocations()
    }
}