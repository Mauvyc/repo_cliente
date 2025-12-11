package com.example.serviconnecta.feature.client.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.domain.model.Booking
import com.example.serviconnecta.feature.client.domain.usecase.GetBookingByIdUseCase
import com.example.serviconnecta.feature.client.domain.usecase.SubmitReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WriteReviewUiState(
    val isLoading: Boolean = false,
    val booking: Booking? = null,
    val serviceRating: Int = 0,           // Calificación del servicio
    val providerRating: Int = 0,          // Calificación del proveedor
    val selectedHighlights: Set<String> = emptySet(), // Highlights seleccionados
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val submitSuccess: Boolean = false,
    val error: String? = null
)

class WriteReviewViewModel(
    private val getBookingByIdUseCase: GetBookingByIdUseCase,
    private val submitReviewUseCase: SubmitReviewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WriteReviewUiState())
    val uiState: StateFlow<WriteReviewUiState> = _uiState.asStateFlow()

    fun loadBooking(bookingId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val booking = getBookingByIdUseCase(bookingId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    booking = booking
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "No se pudo cargar la reserva"
                )
            }
        }
    }

    fun updateServiceRating(rating: Int) {
        _uiState.value = _uiState.value.copy(serviceRating = rating)
    }

    fun updateProviderRating(rating: Int) {
        _uiState.value = _uiState.value.copy(providerRating = rating)
    }

    fun toggleHighlight(highlight: String) {
        val currentHighlights = _uiState.value.selectedHighlights.toMutableSet()
        if (currentHighlights.contains(highlight)) {
            currentHighlights.remove(highlight)
        } else {
            currentHighlights.add(highlight)
        }
        _uiState.value = _uiState.value.copy(selectedHighlights = currentHighlights)
    }

    fun updateComment(comment: String) {
        _uiState.value = _uiState.value.copy(comment = comment)
    }

    fun submitReview(requestId: String) {
        if (_uiState.value.serviceRating == 0) {
            _uiState.value = _uiState.value.copy(error = "Por favor califica el servicio")
            return
        }

        if (_uiState.value.providerRating == 0) {
            _uiState.value = _uiState.value.copy(error = "Por favor califica al proveedor")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)

            try {
                submitReviewUseCase(
                    requestId = requestId,
                    serviceRating = _uiState.value.serviceRating,
                    providerRating = _uiState.value.providerRating,
                    highlights = _uiState.value.selectedHighlights.toList(),
                    comment = _uiState.value.comment.ifBlank { "Sin comentarios" }
                )

                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    submitSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = e.message ?: "Error al enviar la reseña"
                )
            }
        }
    }
}
