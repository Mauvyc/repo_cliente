package com.example.serviconnecta.feature.client.domain.usecase

import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.ServiceItem

class GetServiceDetailUseCase(
    private val repository: ClientServicesRepository
) {
    suspend operator fun invoke(serviceId: String): ServiceItem {
        return repository.getServiceDetail(serviceId)
    }
}