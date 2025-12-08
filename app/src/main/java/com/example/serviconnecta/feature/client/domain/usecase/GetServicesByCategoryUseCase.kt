package com.example.serviconnecta.feature.client.domain.usecase

import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.ServiceItem

class GetServicesByCategoryUseCase(
    private val repository: ClientServicesRepository
) {
    suspend operator fun invoke(categoryId: String): Pair<String, List<ServiceItem>> {
        return repository.getServicesByCategory(categoryId)
    }
}