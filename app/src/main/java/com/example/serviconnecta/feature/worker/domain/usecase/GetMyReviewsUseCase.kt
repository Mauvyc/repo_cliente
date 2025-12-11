package com.example.serviconnecta.feature.worker.domain.usecase

import com.example.serviconnecta.feature.worker.data.WorkerRepository
import com.example.serviconnecta.feature.worker.domain.model.Review

class GetMyReviewsUseCase(
    private val repository: WorkerRepository
) {
    suspend operator fun invoke(): Result<List<Review>> {
        return try {
            val reviews = repository.getMyReviews()
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
