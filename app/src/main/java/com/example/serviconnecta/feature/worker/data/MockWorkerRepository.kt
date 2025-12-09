package com.example.serviconnecta.feature.worker.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.serviconnecta.feature.worker.domain.model.*
import kotlinx.coroutines.delay
// Eliminados imports no usados: Flow, flow

object MockWorkerRepository {

    private val mockServices = mutableListOf(
        Service(
            id = "srv_001",
            title = "Revisión de cables electricos",
            description = "Atendemos fallas eléctricas e instalamos tomacorrientes, interruptores y luminarias. Técnicos certificados, llegada el mismo día, cotización previa y garantía de 30 días. Trabajo seguro y materiales de calidad.",
            category = ServiceCategory(id = "692b8dc198d59291c777649e", name = "Servicio de electricidad"),
            price = 20.0,
            currency = "PEN",
            status = ServiceStatus.ACTIVE,
            pendingRequestsCount = 2,
            imageUrl = null
        ),
        Service(
            id = "srv_002",
            title = "Instalación de tomacorrientes",
            description = "Atendemos fallas eléctricas e instalamos tomacorrientes, interruptores y luminarias. Técnicos certificados, llegada el mismo día, cotización previa y garantía de 30 días. Trabajo seguro y materiales de calidad.",
            category = ServiceCategory(id = "692b8dc198d59291c777649e", name = "Servicio de electricidad"),
            price = 20.0,
            currency = "PEN",
            status = ServiceStatus.PAUSED,
            pendingRequestsCount = 0,
            imageUrl = null
        ),
        Service(
            id = "srv_003",
            title = "Verificación de fuga electrica",
            description = "Atendemos fallas eléctricas e instalamos tomacorrientes, interruptores y luminarias. Técnicos certificados, llegada el mismo día, cotización previa y garantía de 30 días. Trabajo seguro y materiales de calidad.",
            category = ServiceCategory(id = "692b8dc198d59291c777649e", name = "Servicio de electricidad"),
            price = 20.0,
            currency = "PEN",
            status = ServiceStatus.ACTIVE,
            pendingRequestsCount = 1,
            imageUrl = null
        )
    )

    // Actualizado para coincidir con la definición actual de ServiceRequest en WorkerModels.kt
    // data class ServiceRequest(
    //    val requestId: String,
    //    val serviceTitle: String,
    //    val clientName: String,
    //    val date: String,
    //    val timeRange: String,
    //    val location: String
    // )
    private val mockRequests = mutableListOf(
        ServiceRequest(
            requestId = "req_001",
            serviceTitle = "Revisión de cables electricos",
            clientName = "Ricardo Morales",
            date = "15/10/2025",
            timeRange = "15:00 - 17:00 hrs",
            location = "Avenida Test, Lima, San Miguel"
        ),
        ServiceRequest(
            requestId = "req_002",
            serviceTitle = "Revisión de cables electricos",
            clientName = "Jefferson Marquez",
            date = "16/10/2025",
            timeRange = "10:00 - 12:00 hrs",
            location = "Callao, Callao"
        ),
        ServiceRequest(
            requestId = "req_003",
            serviceTitle = "Revisión de cables electricos",
            clientName = "Alan Escribas",
            date = "17/10/2025",
            timeRange = "14:00 - 16:00 hrs",
            location = "Callao, La Perla"
        ),
        ServiceRequest(
            requestId = "req_004",
            serviceTitle = "Revisión de cables electricos",
            clientName = "Ivan Principe",
            date = "18/10/2025",
            timeRange = "11:00 - 13:00 hrs",
            location = "Lima, San Miguel"
        ),
        ServiceRequest(
            requestId = "req_005",
            serviceTitle = "Revisión de cables electricos",
            clientName = "Rissel Nieto",
            date = "19/10/2025",
            timeRange = "16:00 - 18:00 hrs",
            location = "Lima, San Miguel"
        )
    )

    private val mockReviews = listOf(
        Review(
            id = "rev_001",
            clientId = "client_001",
            clientName = "Courtney Henry",
            clientPhoto = null,
            serviceId = "srv_001",
            serviceName = "Revisión de cables electricos",
            rating = 5,
            comment = "Revisaron el tablero y reemplazaron el diferencial. Todo con garantía y explicación clara.",
            date = "12/10/25"
        ),
        Review(
            id = "rev_002",
            clientId = "client_002",
            clientName = "Cameron Williamson",
            clientPhoto = null,
            serviceId = "srv_002",
            serviceName = "Instalación de tomacorrientes",
            rating = 4,
            comment = "Comentario de prueba",
            date = "15/09/25"
        ),
        Review(
            id = "rev_003",
            clientId = "client_003",
            clientName = "Jane Cooper",
            clientPhoto = null,
            serviceId = "srv_003",
            serviceName = "Verificación de fuga electrica",
            rating = 3,
            comment = "Comentario de prueba",
            date = "16/08/25"
        )
    )

    suspend fun getMyServices(): Result<List<Service>> {
        delay(500) // Simular latencia de red
        return Result.success(mockServices)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createService(
        title: String,
        description: String,
        categoryId: String,
        price: Double,
        imageBase64: String?
    ): Result<Service> {
        delay(500)
        val categoryName = when(categoryId) {
            "692b8dc198d59291c777649e" -> "Servicio de electricidad"
            "692b8dc198d59291c777649d" -> "Servicio de gasfitería"
            "692b8dc198d59291c777649f" -> "Servicio de albañilería"
            else -> "Servicio de electricidad"
        }
        val newService = Service(
            id = "srv_${System.currentTimeMillis()}",
            title = title,
            description = description,
            category = ServiceCategory(id = categoryId, name = categoryName),
            price = price,
            currency = "PEN",
            status = ServiceStatus.ACTIVE,
            pendingRequestsCount = 0,
            imageUrl = imageBase64
        )
        mockServices.add(0, newService)
        return Result.success(newService)
    }

    suspend fun updateService(
        serviceId: String,
        title: String? = null,
        description: String? = null,
        price: Double? = null,
        status: String? = null
    ): Result<Service> {
        delay(500)
        val index = mockServices.indexOfFirst { it.id == serviceId }
        if (index != -1) {
            val existingService = mockServices[index]
            val updatedService = existingService.copy(
                title = title ?: existingService.title,
                description = description ?: existingService.description,
                price = price ?: existingService.price,
                status = when(status) {
                    "ACTIVE" -> ServiceStatus.ACTIVE
                    "PAUSED" -> ServiceStatus.PAUSED
                    else -> existingService.status
                }
            )
            mockServices[index] = updatedService
            return Result.success(updatedService)
        }
        return Result.failure(Exception("Service not found"))
    }

    suspend fun getServiceById(serviceId: String): Result<Service> {
        delay(300)
        val service = mockServices.find { it.id == serviceId }
        return if (service != null) {
            Result.success(service)
        } else {
            Result.failure(Exception("Service not found"))
        }
    }

    suspend fun deleteService(serviceId: String): Result<Unit> {
        delay(300)
        mockServices.removeAll { it.id == serviceId }
        return Result.success(Unit)
    }

    suspend fun getRequests(): Result<List<ServiceRequest>> {
        delay(500)
        return Result.success(mockRequests)
    }

    suspend fun acceptRequest(requestId: String): Result<ServiceRequest> {
        delay(500)
        val index = mockRequests.indexOfFirst { it.requestId == requestId }
        if (index != -1) {
            // ServiceRequest actual no tiene estado (status) en la definicion, asi que no podemos hacer copy(status = ...)
            // Si quieres simularlo, tendrias que cambiar el modelo o simplemente devolver el request tal cual.
            // Asumiendo que el modelo NO tiene status por ahora (segun WorkerModels.kt):
            val request = mockRequests[index]
            // Simplemente devolvemos el request encontrado, ya que no hay campo status para actualizar en el modelo actual
            return Result.success(request)
        }
        return Result.failure(Exception("Request not found"))
    }

    suspend fun rejectRequest(requestId: String): Result<ServiceRequest> {
        delay(500)
        val index = mockRequests.indexOfFirst { it.requestId == requestId }
        if (index != -1) {
            // Igual que acceptRequest, no hay campo status en el modelo actual
            val request = mockRequests[index]
            return Result.success(request)
        }
        return Result.failure(Exception("Request not found"))
    }

    suspend fun getMyReviews(): Result<List<Review>> {
        delay(500)
        return Result.success(mockReviews)
    }

    // El modelo actual ServiceRequest no tiene status, así que esta logica no aplica tal cual
    // Se puede dejar devolviendo el primero o null, o eliminar.
    // Para mantener compatibilidad con llamadas, devolvemos el primero o null
    fun getNextRequest(): ServiceRequest? {
        return mockRequests.firstOrNull()
    }
}
