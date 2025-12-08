package com.example.serviconnecta.feature.client.domain.usecase

import com.example.serviconnecta.feature.client.data.ClientHomeData
import com.example.serviconnecta.feature.client.data.ClientServicesRepository

class GetClientHomeUseCase(
    private val repository: ClientServicesRepository
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): ClientHomeData {
        return repository.getHome(latitude, longitude)
    }
}
