package com.example.serviconnecta.feature.client.domain.usecase

import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.Booking

class GetClientReservationsUseCase(
    private val repository: ClientServicesRepository
) {
    suspend operator fun invoke(
        status: String = "ALL"
    ): List<Booking> {
        return repository.getClientReservations(
            status = status,
            page = 1,
            pageSize = 20
        )
    }
}