package com.example.serviconnecta.feature.client.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.ServiceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AllServicesUiState(
    val isLoading: Boolean = false,
    val services: List<ServiceItem> = emptyList(),
    val error: String? = null
)

class AllServicesViewModel(
    private val clientServicesRepository: ClientServicesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllServicesUiState())
    val uiState: StateFlow<AllServicesUiState> = _uiState.asStateFlow()

    fun loadAllServices() {
        viewModelScope.launch {
            android.util.Log.d("AllServicesVM", "═══════════════════════════════════════")
            android.util.Log.d("AllServicesVM", "loadAllServices() iniciado")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // ✅ CORREGIDO: Ahora obtiene TODOS los servicios del backend
                android.util.Log.d("AllServicesVM", "Llamando a clientServicesRepository.getAllServices()")
                val allServices = clientServicesRepository.getAllServices()

                android.util.Log.d("AllServicesVM", "✅ Todos los servicios obtenidos exitosamente")
                android.util.Log.d("AllServicesVM", "Total servicios recibidos: ${allServices.size}")

                allServices.forEachIndexed { index, service ->
                    android.util.Log.d("AllServicesVM", "  Servicio $index: ${service.id} - ${service.title} (rating: ${service.rating})")
                }

                // Ordenar por rating (descendente) y luego alfabéticamente
                val sortedServices = allServices
                    .sortedWith(
                        compareByDescending<ServiceItem> { it.rating }
                            .thenBy { it.title.lowercase() }
                    )

                android.util.Log.d("AllServicesVM", "Servicios ordenados: ${sortedServices.size}")
                android.util.Log.d("AllServicesVM", "✅ Estado actualizado correctamente")
                android.util.Log.d("AllServicesVM", "═══════════════════════════════════════")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    services = sortedServices
                )
            } catch (e: Exception) {
                android.util.Log.e("AllServicesVM", "❌ ERROR al cargar servicios", e)
                android.util.Log.e("AllServicesVM", "Mensaje: ${e.message}")
                android.util.Log.e("AllServicesVM", "Clase: ${e.javaClass.simpleName}")
                android.util.Log.e("AllServicesVM", "═══════════════════════════════════════")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }
}
