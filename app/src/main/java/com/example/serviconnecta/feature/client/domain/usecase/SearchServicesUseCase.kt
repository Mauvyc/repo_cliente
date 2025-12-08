package com.example.serviconnecta.feature.client.domain.usecase

import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.ServiceItem

/**
 * Use case para buscar servicios por query de texto.
 *
 * @param repository Repositorio de servicios del cliente
 */
class SearchServicesUseCase(
    private val repository: ClientServicesRepository
) {
    /**
     * Busca servicios que coincidan con el query proporcionado.
     *
     * @param query Texto de búsqueda (nombre de servicio, categoría, proveedor, etc.)
     * @return Lista de servicios que coinciden con la búsqueda
     * @throws Exception Si ocurre un error en la búsqueda
     */
    suspend operator fun invoke(query: String): List<ServiceItem> {
        // Validación: query no puede estar vacío
        require(query.isNotBlank()) { "El query de búsqueda no puede estar vacío" }

        return repository.searchServices(query)
    }
}
