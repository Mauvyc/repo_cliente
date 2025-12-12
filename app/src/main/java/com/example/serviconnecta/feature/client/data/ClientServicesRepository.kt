package com.example.serviconnecta.feature.client.data

import com.example.serviconnecta.core.network.StandardResponse
import com.example.serviconnecta.core.utils.FormatUtils
import com.example.serviconnecta.feature.client.data.remote.CategoryServicesResponseDto
import com.example.serviconnecta.feature.client.data.remote.ClientApiService
import com.example.serviconnecta.feature.client.data.remote.ClientHomeResponseDto
import com.example.serviconnecta.feature.client.data.remote.CreateLocationRequestDto
import com.example.serviconnecta.feature.client.data.remote.CreatePaymentMethodRequestDto
import com.example.serviconnecta.feature.client.data.remote.CreateServiceRequestDto
import com.example.serviconnecta.feature.client.data.remote.PaymentMethodsResponseDto
import com.example.serviconnecta.feature.client.data.remote.PriceSummaryDto
import com.example.serviconnecta.feature.client.data.remote.ProviderDetailResponseDto
import com.example.serviconnecta.feature.client.data.remote.ScheduledTimeRangeDto
import com.example.serviconnecta.feature.client.data.remote.SearchServicesResponseDto
import com.example.serviconnecta.feature.client.data.remote.SubmitReviewRequestDto
import com.example.serviconnecta.feature.client.data.remote.toDomain
import com.example.serviconnecta.feature.client.data.remote.toDomainBooking
import com.example.serviconnecta.feature.client.data.remote.toDomainLocation
import com.example.serviconnecta.feature.client.data.remote.toDomainProviderDetail
import com.example.serviconnecta.feature.client.data.remote.toDomainServiceItem
import com.example.serviconnecta.feature.client.domain.model.Booking
import com.example.serviconnecta.feature.client.domain.model.Category
import com.example.serviconnecta.feature.client.domain.model.Location
import com.example.serviconnecta.feature.client.domain.model.PaymentMethod
import com.example.serviconnecta.feature.client.domain.model.Provider
import com.example.serviconnecta.feature.client.domain.model.ProviderDetail
import com.example.serviconnecta.feature.client.domain.model.ServiceItem

data class ClientHomeData(
    val userName: String,
    val deliveryAddressLabel: String,
    val categories: List<Category>,
    val topServices: List<ServiceItem>,
    val featuredWorkers: List<Provider>
)
class ClientServicesRepository(
    private val clientApi: ClientApiService,
    private val reviewedServicesPreferences: com.example.serviconnecta.core.datastore.ReviewedServicesPreferences
) {
    suspend fun getHome(
        latitude: Double,
        longitude: Double,
        limit: Int? = null,
        pageSize: Int? = null,
        includeAll: Boolean? = null,
        minRating: Double? = null
    ): ClientHomeData {
        val response = clientApi.getClientHome(latitude, longitude, limit, pageSize, includeAll, minRating)

        if (!response.success) {
            throw IllegalStateException(response.message)
        }

        val data = response.data
            ?: throw IllegalStateException("Respuesta vacía del servidor")

        // 👇 aquí antes asumías que no era null
        val deliveryLabel = data.delivery_address?.label ?: "Selecciona tu ubicación"

        android.util.Log.d("ClientServicesRepo", "Home returned ${data.categories.size} categories")
        data.categories.forEach { cat ->
            android.util.Log.d("ClientServicesRepo", "Category: id=${cat.id}, name=${cat.name}")
        }

        val categories = data.categories
            .filter { cat ->
                // Filtrar la categoría de limpieza
                val normalizedName = cat.name
                    .lowercase()
                    .replace("á", "a")
                    .replace("é", "e")
                    .replace("í", "i")
                    .replace("ó", "o")
                    .replace("ú", "u")
                normalizedName != "limpieza"
            }
            .map { cat ->
                Category(
                    id = cat.id,
                    name = cat.name,
                    slug = cat.name
                        .lowercase()
                        .replace("á", "a")
                        .replace("é", "e")
                        .replace("í", "i")
                        .replace("ó", "o")
                        .replace("ú", "u"),
                    iconName = cat.icon_url
                )
            }

        android.util.Log.d("ClientServicesRepo", "Home returned ${data.top_services.size} services")
        data.top_services.forEachIndexed { index, srv ->
            android.util.Log.d("ClientServicesRepo", "Service $index: id=${srv.id}, title=${srv.title}, category_id=${srv.category_id}")
        }

        val services = data.top_services.map { srv ->
            ServiceItem(
                id = srv.id,
                title = srv.title,
                description = "",
                category = srv.category_id,
                price = srv.price,
                imageUrl = srv.image_url,
                rating = srv.rating,
                reviewCount = srv.reviews_count,
                provider = Provider(
                    id = srv.provider.id,
                    name = srv.provider.name,
                    photo = srv.provider.avatar_url,
                    specialty = srv.provider.profession,
                    rating = srv.rating
                )
            )
        }

        val featuredWorkers = data.featured_workers.map { w ->
            Provider(
                id = w.id,
                name = w.name,
                photo = w.avatar_url,
                specialty = w.profession,
                rating = w.rating
            )
        }

        return ClientHomeData(
            userName = data.user.full_name,
            deliveryAddressLabel = deliveryLabel,
            categories = categories,
            topServices = services,
            featuredWorkers = featuredWorkers
        )
    }

    /**
     * Obtiene servicios filtrados por categoría usando el endpoint específico del API.
     *
     * @param categoryId ID de la categoría
     * @return Par con el nombre de la categoría y la lista de servicios filtrados
     */
    suspend fun getServicesByCategory(categoryId: String): Pair<String, List<ServiceItem>> {
        val response = clientApi.getServicesByCategory(
            categoryId = categoryId,
            page = 1,
            pageSize = 100,
            search = ""
        )

        if (!response.success) {
            android.util.Log.e("ClientServicesRepo", "❌ Error: ${response.message}")
            throw IllegalStateException(response.message)
        }

        val data = response.data
            ?: throw IllegalStateException("Respuesta vacía del servidor")

        val categoryName = data.category.name

        val services = data.services.map { dto ->
            ServiceItem(
                id = dto.id,
                title = dto.title,
                description = "",
                category = categoryName,
                price = dto.price,
                imageUrl = dto.image_url,
                rating = dto.rating,
                reviewCount = dto.reviews_count,
                provider = Provider(
                    id = dto.provider.id,
                    name = dto.provider.name,
                    photo = dto.provider.avatar_url,
                    specialty = dto.provider.profession,
                    rating = dto.rating
                )
            )
        }

        return categoryName to services
    }

    suspend fun getServiceDetail(serviceId: String): ServiceItem {
        val response = clientApi.getServiceDetail(serviceId)

        if (!response.success || response.data == null) {
            val message = response.errors?.joinToString(", ")
                ?: "Error obteniendo detalle del servicio"
            throw Exception(message)
        }

        return response.data.toDomainServiceItem()
    }

    suspend fun getClientReservations(
        status: String = "ALL",
        page: Int = 1,
        pageSize: Int = 10
    ): List<Booking> {
        val response = clientApi.getServiceRequests(status, page, pageSize)

        if (!response.success) {
            throw Exception(response.errors?.joinToString())
        }

        return response.data?.requests?.map { it.toDomainBooking() } ?: emptyList()
    }

    /**
     * Obtiene una reserva específica por su ID.
     * Busca en todas las reservas del cliente.
     *
     * @param bookingId ID de la reserva (request_id)
     * @return Booking encontrado
     * @throws Exception si no se encuentra la reserva
     */
    suspend fun getBookingById(bookingId: String): Booking {
        // Obtener todas las reservas con un page_size grande para asegurar que encontremos la reserva
        val allBookings = getClientReservations(status = "ALL", page = 1, pageSize = 100)

        val booking = allBookings.find { it.id == bookingId }

        if (booking != null) {
            return booking
        } else {
            throw Exception("No se encontró la reserva con ID: $bookingId")
        }
    }

    suspend fun getClientLocations(): List<Location> {
        val response = clientApi.getClientLocations()
        if (!response.success || response.data == null) {
            throw Exception(response.errors?.joinToString()?.ifBlank { "Error al obtener ubicaciones" })
        }
        return response.data.locations.map { it.toDomainLocation() }
    }

    suspend fun createLocation(
        label: String,
        address: String
    ): Location {
        val defaultLatitude = -12.0464
        val defaultLongitude = -77.0428

        val body = CreateLocationRequestDto(
            label = label,
            full_address = address,
            latitude = defaultLatitude,
            longitude = defaultLongitude,
            set_as_default = false
        )

        val response = clientApi.createLocation(body)

        if (!response.success) {
            val errorMsg = response.errors?.firstOrNull()
                ?: response.message
                ?: "Error al crear ubicación"
            throw Exception(errorMsg)
        }

        val dto = response.data
            ?: throw Exception("Respuesta inválida al crear ubicación")

        return dto.toDomainLocation()
    }

    suspend fun getPaymentMethods(): List<PaymentMethod> {
        val response = clientApi.getPaymentMethods()

        if (!response.success) {
            val msg = response.errors?.firstOrNull()
            throw Exception(msg ?: response.message)
        }

        val dto = response.data ?: PaymentMethodsResponseDto()

        android.util.Log.d("BookingVM", "DTO.payment_methods count=${dto.payment_methods.size}")

        return dto.payment_methods.map { it.toDomain() }
    }

    suspend fun createCardPaymentMethod(
        cardNumber: String,
        cardHolderName: String,
        expMonth: Int,
        expYear: Int,
        cvv: String
    ): PaymentMethod {
        val last4 = cardNumber.takeLast(4)
        val label = "Visa terminada en $last4"

        val body = CreatePaymentMethodRequestDto(
            type = "CARD_SIMULATED",
            label = label,
            cardNumber = cardNumber,
            cardHolderName = cardHolderName,
            expMonth = expMonth,
            expYear = expYear,
            cvv = cvv
        )

        val response = clientApi.createPaymentMethod(body)

        if (!response.success) {
            val msg = response.errors?.firstOrNull()
            throw Exception(msg ?: "Error al crear método de pago")
        }

        val dto = response.data
            ?: throw Exception("Respuesta inválida al crear método de pago")

        return dto.toDomain()
    }

    suspend fun createServiceRequest(
        serviceId: String,
        locationId: String,
        scheduledDate: String,
        timeStart: String,
        timeEnd: String,
        paymentMethodId: String,
        notes: String?,
        currency: String,
        subtotal: Double,
        discount: Double,
        total: Double
    ): String {
        val body = CreateServiceRequestDto(
            service_id = serviceId,
            location_id = locationId,
            scheduled_date = scheduledDate,
            scheduled_time_range = ScheduledTimeRangeDto(
                start = timeStart,
                end = timeEnd
            ),
            payment_method_id = paymentMethodId,
            notes = notes,
            price_summary = PriceSummaryDto(
                currency = currency,
                subtotal = subtotal,
                discount = discount,
                total = total
            )
        )

        val response = clientApi.createServiceRequest(body)
        if (!response.success || response.data == null) {
            throw Exception(response.errors?.joinToString()?.ifBlank { "Error al crear solicitud" })
        }

        return response.data.request_id
    }

    /**
     * Busca servicios que coincidan con el query proporcionado.
     *
     * SERVICIOS DISPONIBLES PARA BÚSQUEDA (5 servicios totales):
     * - Reparación de tablero de interruptores (Electricidad)
     * - Servicio de electricista (Electricidad)
     * - Instalación del interruptor de CA (Electricidad)
     * - Revisión de cables eléctricos (Electricidad)
     * - Detección y reparación de fugas de agua (Gasfitería)
     *
     * El endpoint /services/search NO existe en el backend (404).
     * Usa los servicios del home y los filtra localmente por título, proveedor o especialidad.
     *
     * @param query Texto de búsqueda
     * @return Lista de servicios que coinciden con la búsqueda
     */
    suspend fun searchServices(query: String): List<ServiceItem> {
        android.util.Log.d("ClientServicesRepo", "═══════════════════════════════════════")
        android.util.Log.d("ClientServicesRepo", "🔍 searchServices - query: '$query'")

        if (query.isBlank()) {
            android.util.Log.d("ClientServicesRepo", "⚠️ Empty query, returning empty list")
            android.util.Log.d("ClientServicesRepo", "═══════════════════════════════════════")
            return emptyList()
        }

        // Obtener TODOS los servicios disponibles de todas las categorías
        val allServices = getAllServices()

        android.util.Log.d("ClientServicesRepo", "📊 Total services available: ${allServices.size}")

        // Filtrar servicios localmente por query (case-insensitive)
        val queryLower = query.lowercase()

        val filteredServices = allServices.filter { service ->
            val titleMatch = service.title.lowercase().contains(queryLower)
            val categoryMatch = service.category.lowercase().contains(queryLower)
            val providerMatch = service.provider.name.lowercase().contains(queryLower)
            val specialtyMatch = service.provider.specialty.lowercase().contains(queryLower)
            val matches = titleMatch || categoryMatch || providerMatch || specialtyMatch

            android.util.Log.d("ClientServicesRepo", "  Service: ${service.title} | title:$titleMatch category:$categoryMatch provider:$providerMatch specialty:$specialtyMatch | matches: $matches")
            matches
        }

        android.util.Log.d("ClientServicesRepo", "✅ Found ${filteredServices.size} services matching '$query'")
        filteredServices.forEach { service ->
            android.util.Log.d("ClientServicesRepo", "  ✓ ${service.title} - ${service.provider.name}")
        }
        android.util.Log.d("ClientServicesRepo", "═══════════════════════════════════════")

        return filteredServices
    }

    /**
     * Envía una reseña para un servicio completado.
     *
     * @param requestId ID de la solicitud de servicio (service request)
     * @param rating Calificación de 1 a 5 estrellas
     * @param comment Comentario de la reseña
     * @throws Exception Si ocurre un error al enviar la reseña
     */
    suspend fun submitReview(
        requestId: String,
        serviceRating: Int,
        providerRating: Int,
        highlights: List<String>,
        comment: String
    ) {
        try {
            val requestDto = SubmitReviewRequestDto(
                service_rating = serviceRating,
                provider_rating = providerRating,
                highlights = highlights,
                comment = comment
            )

            val response = clientApi.submitReview(requestId, requestDto)

            if (!response.success || response.data == null) {
                val errorMsg = response.errors?.joinToString(", ")
                    ?: response.message
                    ?: "Error al enviar la reseña"
                throw Exception(errorMsg)
            }

            // Éxito - marcar como reviewed localmente
            reviewedServicesPreferences.markAsReviewed(requestId)
            android.util.Log.d(
                "ClientServicesRepository",
                "Reseña enviada exitosamente: ${response.data.review_id}, marcado como reviewed: $requestId"
            )
        } catch (e: Exception) {
            android.util.Log.e(
                "ClientServicesRepository",
                "Error al enviar reseña: ${e.message}"
            )
            throw Exception("No se pudo enviar la reseña: ${e.message}")
        }
    }

    /**
     * Obtiene el detalle completo de un proveedor incluyendo sus servicios.
     *
     * NOTA: El backend actualmente NO tiene implementado el endpoint GET /services/providers/{id}.
     * Esta implementación está preparada para conectarse al endpoint cuando esté disponible.
     *
     * Mientras tanto, usa un fallback que obtiene los datos del home y filtra por proveedor.
     *
     * @param providerId ID del proveedor
     * @return ProviderDetail con información del proveedor y sus servicios
     * @throws Exception Si ocurre un error al obtener los datos
     */
    suspend fun getProviderDetail(providerId: String): ProviderDetail {
        return try {
            // Intentar llamar al endpoint del backend
            val response = clientApi.getProviderDetail(providerId)

            if (!response.success || response.data == null) {
                throw Exception("Endpoint de detalle de proveedor no disponible")
            }

            response.data.toDomainProviderDetail()
        } catch (e: Exception) {
            // Fallback: Obtener datos del home y filtrar por proveedor
            android.util.Log.w(
                "ClientServicesRepository",
                "Endpoint /services/providers/{id} no disponible. Usando fallback: ${e.message}"
            )

            getProviderDetailFallback(providerId)
        }
    }

    /**
     * Fallback temporal para obtener detalle del proveedor mientras el backend no tenga el endpoint.
     *
     * Obtiene datos del home y filtra servicios por proveedor.
     * Este método se eliminará cuando el backend implemente el endpoint real.
     */
    private suspend fun getProviderDetailFallback(providerId: String): ProviderDetail {
        // Coordenadas por defecto (Lima, Perú)
        val homeData = getHome(latitude = -12.0464, longitude = -77.0428)

        // Buscar el proveedor en featured workers
        val provider = homeData.featuredWorkers.firstOrNull { it.id == providerId }
            ?: throw Exception("Proveedor no encontrado")

        // Filtrar servicios del proveedor
        val providerServices = homeData.topServices.filter { it.provider.id == providerId }

        return ProviderDetail(
            provider = provider,
            services = providerServices,
            description = "Profesional con más de ${provider.yearsExperience ?: 5} años de experiencia en ${provider.specialty.lowercase()}. " +
                    "Comprometido con la calidad y la satisfacción del cliente. Trabajo garantizado.",
            isVerified = true
        )
    }

    /**
     * Obtiene TODOS los servicios disponibles en el backend.
     *
     * IMPORTANTE: Usa el endpoint /services/categories/{category_id}/services
     * para obtener servicios de TODAS las categorías disponibles.
     *
     * Hace 3 llamadas (una por categoría) y combina todos los resultados:
     * - Albañilería: 692b8dc198d59291c777649f
     * - Electricidad: 692b8dc198d59291c777649e
     * - Gasfitería: 692b8dc198d59291c777649d
     *
     * @return Lista con todos los servicios disponibles en el backend
     */
    suspend fun getAllServices(): List<ServiceItem> {
        android.util.Log.d("ClientServicesRepo", "═══════════════════════════════════════")
        android.util.Log.d("ClientServicesRepo", "getAllServices() - Obteniendo servicios de TODAS las categorías")

        try {
            // IDs de las 3 categorías disponibles
            val categoryIds = listOf(
                "692b8dc198d59291c777649f", // Albañilería
                "692b8dc198d59291c777649e", // Electricidad
                "692b8dc198d59291c777649d"  // Gasfitería
            )

            val allServices = mutableListOf<ServiceItem>()

            // Obtener servicios de cada categoría usando el método del repositorio
            // que ya sabemos que funciona correctamente
            for (categoryId in categoryIds) {
                try {
                    android.util.Log.d("ClientServicesRepo", "📥 Obteniendo servicios de categoría: $categoryId")

                    // Usar el método getServicesByCategory() del repositorio
                    // en lugar de llamar directamente al API
                    val (categoryName, categoryServices) = getServicesByCategory(categoryId)

                    android.util.Log.d("ClientServicesRepo", "  ✅ ${categoryServices.size} servicios obtenidos de categoría $categoryName")
                    allServices.addAll(categoryServices)
                } catch (e: Exception) {
                    android.util.Log.e("ClientServicesRepo", "  ❌ EXCEPCIÓN con categoría $categoryId", e)
                    android.util.Log.e("ClientServicesRepo", "     Tipo: ${e.javaClass.simpleName}")
                    android.util.Log.e("ClientServicesRepo", "     Mensaje: ${e.message}")
                    e.printStackTrace()
                    // Continuar con la siguiente categoría aunque esta falle
                }
            }

            // Eliminar duplicados por ID (por si un servicio aparece en múltiples categorías)
            val uniqueServices = allServices.distinctBy { it.id }

            android.util.Log.d("ClientServicesRepo", "")
            android.util.Log.d("ClientServicesRepo", "📊 RESUMEN:")
            android.util.Log.d("ClientServicesRepo", "   Total servicios obtenidos: ${allServices.size}")
            android.util.Log.d("ClientServicesRepo", "   Servicios únicos: ${uniqueServices.size}")

            if (uniqueServices.isEmpty()) {
                android.util.Log.w("ClientServicesRepo", "")
                android.util.Log.w("ClientServicesRepo", "⚠️⚠️⚠️ ADVERTENCIA: NO SE OBTUVIERON SERVICIOS ⚠️⚠️⚠️")
                android.util.Log.w("ClientServicesRepo", "Esto causará que se muestre 'No hay servicios disponibles' en la UI")
                android.util.Log.w("ClientServicesRepo", "")
            } else {
                uniqueServices.forEachIndexed { index, service ->
                    android.util.Log.d("ClientServicesRepo", "   ${index + 1}. ${service.title} (rating: ${service.rating})")
                }
            }

            android.util.Log.d("ClientServicesRepo", "═══════════════════════════════════════")

            return uniqueServices
        } catch (e: Exception) {
            android.util.Log.e("ClientServicesRepo", "❌ Error en getAllServices()", e)
            android.util.Log.e("ClientServicesRepo", "Mensaje: ${e.message}")
            android.util.Log.e("ClientServicesRepo", "Tipo: ${e.javaClass.simpleName}")
            android.util.Log.d("ClientServicesRepo", "═══════════════════════════════════════")
            throw e
        }
    }
}
