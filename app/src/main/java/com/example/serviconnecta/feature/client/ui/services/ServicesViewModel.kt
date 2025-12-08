package com.example.serviconnecta.feature.client.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.data.MockClientRepository
import com.example.serviconnecta.feature.client.domain.model.ServiceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ServicesUiState(
    val isLoading: Boolean = false,
    val services: List<ServiceItem> = emptyList(),
    val category: String? = null,
    val searchQuery: String = "",
    val errorMessage: String? = null
)


class ServicesViewModel(
    private val repository: MockClientRepository = MockClientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServicesUiState())
    val uiState: StateFlow<ServicesUiState> = _uiState.asStateFlow()

    fun loadServices(category: String? = null, search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getServices(category, search).fold(
                onSuccess = { services ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            services = services,
                            category = category,
                            searchQuery = search ?: "",
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

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        loadServices(search = query.ifBlank { null })
    }
}
