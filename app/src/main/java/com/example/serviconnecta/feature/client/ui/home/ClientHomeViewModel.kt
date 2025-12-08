package com.example.serviconnecta.feature.client.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.core.datastore.LocationPreferences
import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.Category
import com.example.serviconnecta.feature.client.domain.model.Location
import com.example.serviconnecta.feature.client.domain.model.Provider
import com.example.serviconnecta.feature.client.domain.model.ServiceItem
import com.example.serviconnecta.feature.client.domain.usecase.GetClientHomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ClientHomeUiState(
    val isLoading: Boolean = false,
    val deliveryAddress: String = "",
    val selectedLocationId: String? = null,
    val selectedLocationLatitude: Double? = null,
    val selectedLocationLongitude: Double? = null,
    val categories: List<Category> = emptyList(),
    val bestServices: List<ServiceItem> = emptyList(),
    val featuredWorkers: List<Provider> = emptyList(),
    val error: String? = null
)

class ClientHomeViewModel(
    private val getClientHomeUseCase: GetClientHomeUseCase,
    private val locationPreferences: LocationPreferences,
    private val clientServicesRepository: ClientServicesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientHomeUiState())
    val uiState: StateFlow<ClientHomeUiState> = _uiState

    init {
        loadSelectedLocation()
    }

    private fun loadSelectedLocation() {
        viewModelScope.launch {
            try {
                // Intentar cargar ubicación guardada en DataStore
                val savedAddress = locationPreferences.getSelectedLocationAddress()
                val savedLatitude = locationPreferences.selectedLocationLatitudeFlow.firstOrNull()
                val savedLongitude = locationPreferences.selectedLocationLongitudeFlow.firstOrNull()

                if (savedAddress != null) {
                    // Usar ubicación guardada
                    _uiState.value = _uiState.value.copy(
                        deliveryAddress = savedAddress,
                        selectedLocationLatitude = savedLatitude,
                        selectedLocationLongitude = savedLongitude
                    )
                    // Cargar home con esas coordenadas
                    loadHome(savedLatitude ?: -12.0464, savedLongitude ?: -77.0428)
                } else {
                    // No hay ubicación guardada, cargar del backend la predeterminada
                    loadDefaultLocationFromBackend()
                }
            } catch (e: Exception) {
                // Si falla, usar coordenadas por defecto
                loadHome(-12.0464, -77.0428)
            }
        }
    }

    private fun loadDefaultLocationFromBackend() {
        viewModelScope.launch {
            try {
                val locations = clientServicesRepository.getClientLocations()
                val defaultLocation = locations.firstOrNull { it.isDefault }

                if (defaultLocation != null) {
                    // Guardar ubicación predeterminada
                    locationPreferences.saveSelectedLocation(
                        id = defaultLocation.id,
                        label = defaultLocation.label,
                        address = defaultLocation.address,
                        latitude = defaultLocation.latitude,
                        longitude = defaultLocation.longitude,
                        isDefault = true
                    )
                    // Actualizar estado
                    _uiState.value = _uiState.value.copy(
                        deliveryAddress = defaultLocation.address,
                        selectedLocationId = defaultLocation.id,
                        selectedLocationLatitude = defaultLocation.latitude,
                        selectedLocationLongitude = defaultLocation.longitude
                    )
                    // Cargar home
                    loadHome(
                        defaultLocation.latitude ?: -12.0464,
                        defaultLocation.longitude ?: -77.0428
                    )
                } else {
                    // No hay ubicación predeterminada, usar coordenadas por defecto
                    _uiState.value = _uiState.value.copy(
                        deliveryAddress = "Agrega una ubicación"
                    )
                    loadHome(-12.0464, -77.0428)
                }
            } catch (e: Exception) {
                // Si falla, usar coordenadas por defecto
                _uiState.value = _uiState.value.copy(
                    deliveryAddress = "Ubicación no disponible"
                )
                loadHome(-12.0464, -77.0428)
            }
        }
    }

    fun updateSelectedLocation(location: Location) {
        viewModelScope.launch {
            try {
                // Guardar en DataStore
                locationPreferences.saveSelectedLocation(
                    id = location.id,
                    label = location.label,
                    address = location.address,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    isDefault = location.isDefault
                )
                // Actualizar estado
                _uiState.value = _uiState.value.copy(
                    deliveryAddress = location.address,
                    selectedLocationId = location.id,
                    selectedLocationLatitude = location.latitude,
                    selectedLocationLongitude = location.longitude
                )
                // Recargar home con las nuevas coordenadas
                loadHome(
                    location.latitude ?: -12.0464,
                    location.longitude ?: -77.0428
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadHome(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val data = getClientHomeUseCase(latitude, longitude)

                // Tomar solo los 3 mejores servicios (por rating y alfabéticamente)
                val top3Services = data.topServices
                    .sortedWith(
                        compareByDescending<ServiceItem> { it.rating }
                            .thenBy { it.title.lowercase() }
                    )
                    .take(3)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    categories = data.categories,
                    bestServices = top3Services,
                    featuredWorkers = data.featuredWorkers,
                    error = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar el inicio"
                )
            }
        }
    }

    fun reset() {
        _uiState.value = ClientHomeUiState()
    }
}