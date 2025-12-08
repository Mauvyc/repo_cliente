package com.example.serviconnecta.feature.client.ui.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.data.MockClientRepository
import com.example.serviconnecta.feature.client.domain.model.Booking
import com.example.serviconnecta.feature.client.domain.usecase.GetClientReservationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReservationsUiState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val error: String? = null
)

class ReservationsViewModel(
    private val getClientReservationsUseCase: GetClientReservationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationsUiState())
    val uiState: StateFlow<ReservationsUiState> = _uiState.asStateFlow()

    fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val bookings = getClientReservationsUseCase()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    bookings = bookings,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar reservas"
                )
            }
        }
    }

    fun refresh() = loadBookings()
}

