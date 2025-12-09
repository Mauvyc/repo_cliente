package com.example.serviconnecta.feature.worker.data

import com.example.serviconnecta.feature.worker.data.remote.*
import com.example.serviconnecta.feature.worker.data.remote.ServicesMappers.toDomain
import com.example.serviconnecta.feature.worker.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface WorkerRepository {
    suspend fun getWorkerHomeData(): WorkerHomeResponse
    suspend fun getServices(page: Int = 1, pageSize: Int = 10, status: String = "ALL"): ServicesData
    suspend fun createService(
        title: String,
        description: String,
        categoryId: String,
        price: Double,
        currency: String = "PEN",
        imageBase64: String? = null
    ): Service
    suspend fun updateService(
        serviceId: String,
        title: String? = null,
        description: String? = null,
        price: Double? = null,
        status: String? = null
    ): Service
    suspend fun getServiceRequests(
        status: String = "PENDING_PROVIDER_CONFIRMATION",
        page: Int = 1,
        pageSize: Int = 10
    ): ServiceRequestsData
    suspend fun getServiceRequestDetail(requestId: String): ServiceRequest
    suspend fun acceptServiceRequest(requestId: String, notes: String? = null): Boolean
    suspend fun rejectServiceRequest(requestId: String, reason: String? = null): Boolean
    suspend fun cancelReservation(requestId: String, reason: String? = null): Boolean
}

data class ServiceRequestsData(
    val pagination: Pagination,
    val requests: List<ServiceRequest>
)

class WorkerRepositoryImpl(
    private val apiService: WorkerApiService,
    private val homeMappers: HomeMappers = HomeMappers()
) : WorkerRepository {

    override suspend fun getWorkerHomeData(): WorkerHomeResponse {
        return withContext(Dispatchers.IO) {
            val response = apiService.getHomeData()

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception("Error al obtener los datos del trabajador: ${response.message}")
            }
        }
    }

    override suspend fun getServices(page: Int, pageSize: Int, status: String): ServicesData {
        return withContext(Dispatchers.IO) {
            val response = apiService.getServices(page, pageSize, status)

            if (response.success && response.data != null) {
                val data = response.data
                ServicesData(
                    pagination = Pagination(
                        page = data.pagination.page,
                        pageSize = data.pagination.pageSize,
                        totalItems = data.pagination.totalItems,
                        totalPages = data.pagination.totalPages
                    ),
                    services = data.services.map { it.toDomain() },
                    reservationsSummary = data.reservationsSummary.map { it.toDomain() }
                )
            } else {
                throw Exception("Error al obtener servicios: ${response.message}")
            }
        }
    }

    override suspend fun createService(
        title: String,
        description: String,
        categoryId: String,
        price: Double,
        currency: String,
        imageBase64: String?
    ): Service {
        return withContext(Dispatchers.IO) {
            val media = imageBase64?.let { MediaDto(it) }
            val request = CreateServiceRequest(title, description, categoryId, price, currency, media)
            val response = apiService.createService(request)

            if (response.success && response.data != null) {
                response.data.toDomain()
            } else {
                throw Exception("Error al crear servicio: ${response.message}")
            }
        }
    }

    override suspend fun updateService(
        serviceId: String,
        title: String?,
        description: String?,
        price: Double?,
        status: String?
    ): Service {
        return withContext(Dispatchers.IO) {
            val request = UpdateServiceRequest(title, description, price, status)
            val response = apiService.updateService(serviceId, request)

            if (response.success && response.data != null) {
                response.data.toDomain()
            } else {
                throw Exception("Error al actualizar servicio: ${response.message}")
            }
        }
    }

    override suspend fun getServiceRequests(
        status: String,
        page: Int,
        pageSize: Int
    ): ServiceRequestsData {
        return withContext(Dispatchers.IO) {
            val response = apiService.getServiceRequests(status, page, pageSize)

            if (response.success && response.data != null) {
                val data = response.data
                ServiceRequestsData(
                    pagination = Pagination(
                        page = data.pagination.page,
                        pageSize = data.pagination.pageSize,
                        totalItems = data.pagination.totalItems,
                        totalPages = data.pagination.totalPages
                    ),
                    requests = data.requests.map {
                        ServiceRequestsMappers.run { it.toDomain() }
                    }
                )
            } else {
                throw Exception("Error al obtener solicitudes: ${response.message}")
            }
        }
    }

    override suspend fun getServiceRequestDetail(requestId: String): ServiceRequest {
        return withContext(Dispatchers.IO) {
            val response = apiService.getServiceRequestDetail(requestId)

            if (response.success && response.data != null) {
                ServiceRequestsMappers.run { response.data.toDomain() }
            } else {
                throw Exception("Error al obtener detalle de solicitud: ${response.message}")
            }
        }
    }

    override suspend fun acceptServiceRequest(requestId: String, notes: String?): Boolean {
        return withContext(Dispatchers.IO) {
            val request = AcceptRequestRequest(notes)
            val response = apiService.acceptServiceRequest(requestId, request)

            if (response.success) {
                true
            } else {
                throw Exception("Error al aceptar solicitud: ${response.message}")
            }
        }
    }

    override suspend fun rejectServiceRequest(requestId: String, reason: String?): Boolean {
        return withContext(Dispatchers.IO) {
            val request = RejectRequestRequest(reason)
            val response = apiService.rejectServiceRequest(requestId, request)

            if (response.success) {
                true
            } else {
                throw Exception("Error al rechazar solicitud: ${response.message}")
            }
        }
    }

    override suspend fun cancelReservation(requestId: String, reason: String?): Boolean {
        return withContext(Dispatchers.IO) {
            val request = CancelReservationRequest(reason)
            val response = apiService.cancelReservation(requestId, request)

            if (response.success) {
                true
            } else {
                throw Exception("Error al cancelar reserva: ${response.message}")
            }
        }
    }
}

