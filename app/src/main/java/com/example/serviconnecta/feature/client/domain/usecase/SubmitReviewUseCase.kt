package com.example.serviconnecta.feature.client.domain.usecase

import com.example.serviconnecta.feature.client.data.ClientServicesRepository

/**
 * Use case para enviar una reseña de un servicio completado.
 *
 * @param repository Repositorio de servicios del cliente
 */
class SubmitReviewUseCase(
    private val repository: ClientServicesRepository
) {
    /**
     * Envía una reseña para un servicio completado.
     *
     * @param bookingId ID de la reservación/solicitud de servicio
     * @param rating Calificación de 1 a 5 estrellas
     * @param comment Comentario de la reseña (opcional)
     * @return Unit si la reseña se envió exitosamente
     * @throws IllegalArgumentException Si los parámetros no son válidos
     * @throws Exception Si ocurre un error al enviar la reseña
     */
    suspend operator fun invoke(
        bookingId: String,
        rating: Int,
        comment: String
    ) {
        // Validaciones de negocio
        require(bookingId.isNotBlank()) { "El ID de la reservación no puede estar vacío" }
        require(rating in 1..5) { "La calificación debe estar entre 1 y 5 estrellas" }

        // Llamar al repository para enviar la reseña
        repository.submitReview(
            bookingId = bookingId,
            rating = rating,
            comment = comment
        )
    }
}
