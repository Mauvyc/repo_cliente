package com.example.serviconnecta.feature.worker.data.remote

import com.example.serviconnecta.feature.worker.domain.model.Provider
import com.example.serviconnecta.feature.worker.domain.model.RatingSummary
import com.example.serviconnecta.feature.worker.domain.model.ServiceRequest
import com.example.serviconnecta.feature.worker.domain.model.ServicesSummary
import com.example.serviconnecta.feature.worker.domain.model.WorkerHome

class HomeMappers {

    // Convierte la respuesta completa de la API (WorkerHomeResponseDto) a WorkerHome
    fun toDomain(response: WorkerHomeResponseDto): WorkerHome {
        return WorkerHome(
            provider = toDomain(response.provider), // Aquí aplicamos la conversión
            nextRequest = response.nextRequest?.let { toDomain(it) }, // Aplicamos la conversión solo si existe
            servicesSummary = toDomain(response.servicesSummary), // Conversión de servicios
            ratingSummary = toDomain(response.ratingSummary) // Conversión de calificación
        )
    }

    // Método para convertir ProviderDto a Provider
    fun toDomain(providerDto: ProviderDto): Provider {
        return Provider(
            id = providerDto.id,
            name = providerDto.name,
            avatarUrl = providerDto.avatarUrl,
            profession = providerDto.profession
        )
    }

    // Método para convertir ServiceRequestDto a ServiceRequest
    fun toDomain(serviceRequestDto: ServiceRequestDto): ServiceRequest {
        return ServiceRequest(
            requestId = serviceRequestDto.requestId,
            serviceTitle = serviceRequestDto.serviceTitle,
            clientName = serviceRequestDto.clientName,
            date = serviceRequestDto.date,
            timeRange = serviceRequestDto.time,
            location = serviceRequestDto.location
        )
    }

    // Método para convertir ServicesSummaryDto a ServicesSummary
    fun toDomain(servicesSummaryDto: ServicesSummaryDto): ServicesSummary {
        return ServicesSummary(
            totalServices = servicesSummaryDto.totalServices,
            activeServices = servicesSummaryDto.activeServices,
            pausedServices = servicesSummaryDto.pausedServices
        )
    }

    // Método para convertir RatingSummaryDto a RatingSummary
    fun toDomain(ratingSummaryDto: RatingSummaryDto): RatingSummary {
        return RatingSummary(
            averageRating = ratingSummaryDto.averageRating,
            totalReviews = ratingSummaryDto.totalReviews
        )
    }
}
