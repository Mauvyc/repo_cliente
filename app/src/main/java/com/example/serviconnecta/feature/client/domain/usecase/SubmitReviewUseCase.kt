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
     * @param requestId ID de la solicitud de servicio
     * @param serviceRating Calificación del servicio de 1 a 5 estrellas
     * @param providerRating Calificación del proveedor de 1 a 5 estrellas
     * @param highlights Lista de aspectos destacados
     * @param comment Comentario de la reseña
     * @return Unit si la reseña se envió exitosamente
     * @throws IllegalArgumentException Si los parámetros no son válidos
     * @throws Exception Si ocurre un error al enviar la reseña
     */
    suspend operator fun invoke(
        requestId: String,
        serviceRating: Int,
        providerRating: Int,
        highlights: List<String>,
        comment: String
    ) {
        // Validaciones de negocio
        require(requestId.isNotBlank()) { "El ID de la solicitud no puede estar vacío" }
        require(serviceRating in 1..5) { "La calificación del servicio debe estar entre 1 y 5 estrellas" }
        require(providerRating in 1..5) { "La calificación del proveedor debe estar entre 1 y 5 estrellas" }

        // Llamar al repository para enviar la reseña
        repository.submitReview(
            requestId = requestId,
            serviceRating = serviceRating,
            providerRating = providerRating,
            highlights = highlights,
            comment = comment
        )
    }
}
