package com.example.serviconnecta.feature.client.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.domain.model.ServiceItem
import com.example.serviconnecta.feature.client.domain.usecase.GetServicesByCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ServicesByCategoryUiState(
    val isLoading: Boolean = false,
    val categoryName: String = "",
    val services: List<ServiceItem> = emptyList(),
    val error: String? = null
)

class ServicesByCategoryViewModel(
    private val getServicesByCategoryUseCase: GetServicesByCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServicesByCategoryUiState())
    val uiState: StateFlow<ServicesByCategoryUiState> = _uiState

    fun loadServices(categoryId: String) {
        viewModelScope.launch {
            try {
                android.util.Log.d("ServicesByCategoryVM", "Loading services for categoryId: $categoryId")
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val (categoryName, services) = getServicesByCategoryUseCase(categoryId)

                android.util.Log.d("ServicesByCategoryVM", "Loaded category: $categoryName with ${services.size} services")
                _uiState.value = ServicesByCategoryUiState(
                    isLoading = false,
                    categoryName = categoryName,
                    services = services,
                    error = null
                )
            } catch (e: Exception) {
                android.util.Log.e("ServicesByCategoryVM", "Error loading services: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar servicios"
                )
            }
        }
    }
}
