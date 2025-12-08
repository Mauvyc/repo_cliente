package com.example.serviconnecta.feature.worker.ui.areas

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyAreasUiState(
    val selectedAreas: List<String> = listOf("San Miguel", "Lima", "Callao"),
    val availableAreas: List<String> = listOf(
        "Ate", "Barranco", "Breña", "Callao", "Chorrillos", "El Agustino",
        "Jesús María", "La Molina", "La Victoria", "Lima", "Lince",
        "Los Olivos", "Magdalena", "Miraflores", "Pueblo Libre", "Rimac",
        "San Borja", "San Isidro", "San Juan de Lurigancho", "San Juan de Miraflores",
        "San Luis", "San Martin de Porres", "San Miguel", "Santa Anita",
        "Santiago de Surco", "Surquillo", "Villa El Salvador", "Villa Maria del Triunfo"
    )
)

class MyAreasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MyAreasUiState())
    val uiState: StateFlow<MyAreasUiState> = _uiState.asStateFlow()

    fun toggleArea(area: String) {
        _uiState.update {
            val newSelected = if (it.selectedAreas.contains(area)) {
                it.selectedAreas - area
            } else {
                it.selectedAreas + area
            }
            it.copy(selectedAreas = newSelected.sorted())
        }
    }

    fun isAreaSelected(area: String): Boolean {
        return _uiState.value.selectedAreas.contains(area)
    }
}
