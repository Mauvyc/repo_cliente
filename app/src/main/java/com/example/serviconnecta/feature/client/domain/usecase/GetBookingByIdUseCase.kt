package com.example.serviconnecta.feature.client.domain.usecase

import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.Booking

/**
 * Use case para obtener una reserva específica por su ID.
 *
 * @param repository Repositorio de servicios del cliente
 */
class GetBookingByIdUseCase(
    private val repository: ClientServicesRepository
) {
    /**
     * Obtiene una reserva por su ID.
     *
     * @param bookingId ID de la reserva (request_id)
     * @return Booking encontrado
     * @throws Exception si no se encuentra la reserva
     */
    suspend operator fun invoke(bookingId: String): Booking {
        return repository.getBookingById(bookingId)
    }
}
