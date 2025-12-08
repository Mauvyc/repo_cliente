package com.example.serviconnecta.feature.client.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.domain.model.ServiceItem
import com.example.serviconnecta.feature.client.domain.usecase.SearchServicesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val isLoading: Boolean = false,
    val results: List<ServiceItem> = emptyList(),
    val error: String? = null
)

class SearchViewModel(
    private val searchServicesUseCase: SearchServicesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    /**
     * Busca servicios que coincidan con el query proporcionado.
     * Implementa debouncing para evitar múltiples llamadas al backend.
     *
     * @param query Texto de búsqueda
     */
    fun search(query: String) {
        // Cancelar búsqueda anterior si existe
        searchJob?.cancel()

        // Si el query está vacío, resetear estado
        if (query.isBlank()) {
            _uiState.value = SearchUiState()
            return
        }

        // Lanzar nueva búsqueda con debouncing
        searchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Debounce de 300ms para evitar múltiples llamadas mientras el usuario escribe
            delay(300)

            try {
                val results = searchServicesUseCase(query)

                _uiState.value = SearchUiState(
                    isLoading = false,
                    results = results,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = SearchUiState(
                    isLoading = false,
                    results = emptyList(),
                    error = e.message ?: "Error al buscar servicios"
                )
            }
        }
    }

    /**
     * Limpia el error actual del estado.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
