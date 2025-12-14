package com.example.serviconnecta.feature.client.ui.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.core.datastore.ReviewedServicesPreferences
import com.example.serviconnecta.feature.client.data.MockClientRepository
import com.example.serviconnecta.feature.client.domain.model.Booking
import com.example.serviconnecta.feature.client.domain.usecase.GetClientReservationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ReservationsUiState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val error: String? = null
)

class ReservationsViewModel(
    private val getClientReservationsUseCase: GetClientReservationsUseCase,
    private val reviewedServicesPreferences: ReviewedServicesPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationsUiState())
    val uiState: StateFlow<ReservationsUiState> = _uiState.asStateFlow()

    fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val bookings = getClientReservationsUseCase()

                // Obtener los IDs de servicios ya calificados
                val reviewedIds = reviewedServicesPreferences.reviewedRequestIds.first()

                // Actualizar hasReview basándonos en los IDs guardados localmente
                val updatedBookings = bookings.map { booking ->
                    val hasReview = reviewedIds.contains(booking.id)
                    booking.copy(hasReview = hasReview)
                }

                android.util.Log.d("ReservationsViewModel", "Loaded ${updatedBookings.size} bookings")
                updatedBookings.forEach { booking ->
                    android.util.Log.d("ReservationsViewModel", "  - ${booking.serviceTitle}: status=${booking.status}, hasReview=${booking.hasReview}")
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    bookings = updatedBookings,
                    error = null
                )
            } catch (e: Exception) {
                android.util.Log.e("ReservationsViewModel", "Error loading bookings", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar reservas"
                )
            }
        }
    }

    fun refresh() = loadBookings()
}

