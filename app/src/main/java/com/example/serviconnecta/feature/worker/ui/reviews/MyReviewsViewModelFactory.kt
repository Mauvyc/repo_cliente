package com.example.serviconnecta.feature.worker.ui.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serviconnecta.feature.worker.domain.usecase.GetMyReviewsUseCase

class MyReviewsViewModelFactory(
    private val getMyReviewsUseCase: GetMyReviewsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyReviewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyReviewsViewModel(getMyReviewsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
