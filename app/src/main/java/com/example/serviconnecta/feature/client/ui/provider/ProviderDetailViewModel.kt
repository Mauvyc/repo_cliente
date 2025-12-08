package com.example.serviconnecta.feature.client.ui.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.domain.model.ProviderDetail
import com.example.serviconnecta.feature.client.domain.usecase.GetProviderDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProviderDetailUiState(
    val isLoading: Boolean = false,
    val providerDetail: ProviderDetail? = null,
    val error: String? = null
)

class ProviderDetailViewModel(
    private val getProviderDetailUseCase: GetProviderDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProviderDetailUiState())
    val uiState: StateFlow<ProviderDetailUiState> = _uiState.asStateFlow()

    /**
     * Carga el detalle completo de un proveedor.
     *
     * @param providerId ID del proveedor a cargar
     */
    fun loadProviderDetail(providerId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val providerDetail = getProviderDetailUseCase(providerId)

                _uiState.value = ProviderDetailUiState(
                    isLoading = false,
                    providerDetail = providerDetail,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = ProviderDetailUiState(
                    isLoading = false,
                    providerDetail = null,
                    error = e.message ?: "Error al cargar el perfil del proveedor"
                )
            }
        }
    }

    /**
     * Limpia el estado del ViewModel.
     */
    fun clearState() {
        _uiState.value = ProviderDetailUiState()
    }
}
