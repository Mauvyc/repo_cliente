package com.example.serviconnecta.feature.worker.domain.usecase

import com.example.serviconnecta.feature.worker.data.WorkerRepository

class MarkServiceAsCompletedUseCase(
    private val repository: WorkerRepository
) {
    suspend operator fun invoke(requestId: String, notes: String? = null): Result<Boolean> {
        return try {
            val result = repository.markServiceAsCompleted(requestId, notes)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
