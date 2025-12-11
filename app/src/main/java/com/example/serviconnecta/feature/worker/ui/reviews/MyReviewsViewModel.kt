package com.example.serviconnecta.feature.worker.ui.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviconnecta.feature.worker.domain.model.Review
import com.example.serviconnecta.feature.worker.domain.usecase.GetMyReviewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyReviewsUiState(
    val isLoading: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val averageRating: Double = 0.0,
    val errorMessage: String? = null
)

class MyReviewsViewModel(
    private val getMyReviewsUseCase: GetMyReviewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyReviewsUiState())
    val uiState: StateFlow<MyReviewsUiState> = _uiState.asStateFlow()

    init {
        loadReviews()
    }

    private fun loadReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getMyReviewsUseCase().fold(
                onSuccess = { reviews ->
                    val average = if (reviews.isNotEmpty()) {
                        reviews.map { it.rating }.average()
                    } else {
                        0.0
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            reviews = reviews,
                            averageRating = average,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al cargar reseñas"
                        )
                    }
                }
            )
        }
    }

    fun refresh() {
        loadReviews()
    }
}
