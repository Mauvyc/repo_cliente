package com.example.serviconnecta.feature.client.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.client.data.MockClientRepository
import com.example.serviconnecta.feature.client.domain.model.Booking
import com.example.serviconnecta.feature.client.domain.usecase.SubmitReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WriteReviewUiState(
    val isLoading: Boolean = false,
    val booking: Booking? = null,
    val rating: Int = 0,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val submitSuccess: Boolean = false,
    val error: String? = null
)

class WriteReviewViewModel(
    private val submitReviewUseCase: SubmitReviewUseCase
) : ViewModel() {

    // Mantener MockClientRepository solo para loadBooking hasta que se implemente el UseCase correspondiente
    private val repository = MockClientRepository

    private val _uiState = MutableStateFlow(WriteReviewUiState())
    val uiState: StateFlow<WriteReviewUiState> = _uiState.asStateFlow()

    fun loadBooking(bookingId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = repository.getBookingById(bookingId)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    booking = result.getOrNull()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "No se pudo cargar la reserva"
                )
            }
        }
    }

    fun updateRating(rating: Int) {
        _uiState.value = _uiState.value.copy(rating = rating)
    }

    fun updateComment(comment: String) {
        _uiState.value = _uiState.value.copy(comment = comment)
    }

    fun submitReview(bookingId: String) {
        if (_uiState.value.rating == 0) {
            _uiState.value = _uiState.value.copy(error = "Por favor selecciona una calificación")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)

            try {
                submitReviewUseCase(
                    bookingId = bookingId,
                    rating = _uiState.value.rating,
                    comment = _uiState.value.comment
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
