package com.example.serviconnecta.feature.client.domain.usecase

import com.example.serviconnecta.feature.client.data.ClientServicesRepository
import com.example.serviconnecta.feature.client.domain.model.ProviderDetail

/**
 * Use case para obtener el detalle completo de un proveedor.
 *
 * @param repository Repositorio de servicios del cliente
 */
class GetProviderDetailUseCase(
    private val repository: ClientServicesRepository
) {
    /**
     * Obtiene el detalle completo de un proveedor incluyendo sus servicios.
     *
     * @param providerId ID del proveedor
     * @return ProviderDetail con toda la información del proveedor
     * @throws IllegalArgumentException Si el providerId está vacío
     * @throws Exception Si ocurre un error al obtener los datos
     */
    suspend operator fun invoke(providerId: String): ProviderDetail {
        require(providerId.isNotBlank()) { "El ID del proveedor no puede estar vacío" }

        return repository.getProviderDetail(providerId)
    }
}
