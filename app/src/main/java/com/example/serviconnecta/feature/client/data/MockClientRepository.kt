package com.example.serviconnecta.feature.client.data

import com.example.serviconnecta.feature.client.domain.model.*
import kotlinx.coroutines.delay
import java.text.Normalizer

object MockClientRepository {

    // Función para normalizar texto (quitar tildes y convertir a minúsculas)
    private fun String.normalize(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "")
            .lowercase()
            .trim()
    }

    private val mockCategories = listOf(
        Category("cat_001", "Gasfitería", "gasfiteria", "gasfiteria"),
        Category("cat_002", "Electricidad", "electricidad", "electricidad"),
        Category("cat_003", "Albañilería", "albanileria", "albanileria")
    )

    private val mockProviders = listOf(
        Provider("prov_001", "Devon Lane", null, "Tecnica Electricista", 4.0),
        Provider("prov_002", "Jose Martinez", null, "Gasfitero", 4.5),
        Provider("prov_003", "Pablo Martinez", null, "Tecnico Electricista", 4.0),
        Provider("prov_004", "Marry Jane", null, "Tecnico Electricista", 4.0)
    )

    private val mockServices = listOf(
        // Servicios de Electricidad
        ServiceItem(
            id = "svc_001",
            title = "Servicio de electricista",
            description = "Atendemos fallas eléctricas e instalamos tomacorrientes, interruptores y luminarias. Técnicos certificados, llegada el mismo día, cotización previa y garantía de 30 días. Trabajo seguro y materiales de calidad.",
            category = "Electricidad",
            price = 20.0,
            imageUrl = null,
            rating = 4.0,
            reviewCount = 125,
            provider = mockProviders[0]
        ),
        ServiceItem(
            id = "svc_002",
            title = "Instalación del interruptor de CA",
            description = "Instalamos y reparamos interruptores de aire acondicionado. Servicio rápido y seguro con garantía de 30 días. Técnicos certificados disponibles el mismo día.",
            category = "Electricidad",
            price = 25.0,
            imageUrl = null,
            rating = 4.5,
            reviewCount = 89,
            provider = mockProviders[2]
        ),
        ServiceItem(
            id = "svc_003",
            title = "Revisión de cables eléctricos",
            description = "Revisión completa de instalaciones eléctricas, detección de fallas y reemplazo de cables dañados. Incluye informe técnico y recomendaciones de seguridad.",
            category = "Electricidad",
            price = 30.0,
            imageUrl = null,
            rating = 4.2,
            reviewCount = 67,
            provider = mockProviders[0]
        ),
        ServiceItem(
            id = "svc_004",
            title = "Reparación de tablero de interruptores",
            description = "Mantenimiento y reparación de tableros eléctricos. Reemplazo de breakers, llaves térmicas y cableado interno. Trabajo certificado con garantía.",
            category = "Electricidad",
            price = 35.0,
            imageUrl = null,
            rating = 4.3,
            reviewCount = 145,
            provider = mockProviders[3]
        ),
        ServiceItem(
            id = "svc_005",
            title = "Instalación de luminarias LED",
            description = "Instalación profesional de sistemas de iluminación LED para hogares y oficinas. Ahorro de energía garantizado. Incluye materiales de primera calidad.",
            category = "Electricidad",
            price = 40.0,
            imageUrl = null,
            rating = 4.4,
            reviewCount = 98,
            provider = mockProviders[0]
        ),

        // Servicios de Gasfitería
        ServiceItem(
            id = "svc_006",
            title = "Detección y reparación de fugas de agua",
            description = "Localizamos y reparamos fugas de agua en tuberías, grifos y conexiones. Servicio de emergencia 24/7. Equipos de detección profesional y reparación inmediata.",
            category = "Gasfitería",
            price = 28.0,
            imageUrl = null,
            rating = 4.6,
            reviewCount = 156,
            provider = mockProviders[1]
        ),
        ServiceItem(
            id = "svc_007",
            title = "Instalación de grifería",
            description = "Instalamos y reemplazamos grifos de cocina, baño y lavandería. Incluye sellado hermético y prueba de fugas. Garantía de 60 días en el trabajo.",
            category = "Gasfitería",
            price = 22.0,
            imageUrl = null,
            rating = 4.3,
            reviewCount = 92,
            provider = mockProviders[1]
        ),
        ServiceItem(
            id = "svc_008",
            title = "Destape de desagües y tuberías",
            description = "Desobstrucción de desagües, lavabos, duchas e inodoros. Uso de equipos profesionales (serpentina, hidromáquina). Servicio rápido y limpio.",
            category = "Gasfitería",
            price = 35.0,
            imageUrl = null,
            rating = 4.5,
            reviewCount = 203,
            provider = mockProviders[1]
        ),
        ServiceItem(
            id = "svc_009",
            title = "Instalación de terma eléctrica",
            description = "Instalamos termas eléctricas de todas las marcas. Incluye conexiones de agua fría/caliente y prueba de funcionamiento. Técnicos especializados.",
            category = "Gasfitería",
            price = 45.0,
            imageUrl = null,
            rating = 4.7,
            reviewCount = 78,
            provider = mockProviders[1]
        ),
        ServiceItem(
            id = "svc_010",
            title = "Cambio de tubería y cañerías",
            description = "Reemplazo de tuberías antiguas o dañadas. Trabajamos con PVC, cobre y otros materiales. Incluye prueba hidráulica y garantía de 90 días.",
            category = "Gasfitería",
            price = 50.0,
            imageUrl = null,
            rating = 4.4,
            reviewCount = 134,
            provider = mockProviders[1]
        ),

        // Servicios de Albañilería
        ServiceItem(
            id = "svc_011",
            title = "Construcción y reparación de muros",
            description = "Levantamiento de muros nuevos y reparación de muros existentes. Trabajamos con ladrillo, bloque y drywall. Acabado profesional garantizado.",
            category = "Albañilería",
            price = 60.0,
            imageUrl = null,
            rating = 4.5,
            reviewCount = 87,
            provider = mockProviders[2]
        ),
        ServiceItem(
            id = "svc_012",
            title = "Tarrajeo y enlucido de paredes",
            description = "Tarrajeo de interiores y exteriores. Superficie lisa lista para pintar. Incluye preparación de mezcla y acabado fino. Maestros especializados.",
            category = "Albañilería",
            price = 38.0,
            imageUrl = null,
            rating = 4.3,
            reviewCount = 112,
            provider = mockProviders[2]
        ),
        ServiceItem(
            id = "svc_013",
            title = "Instalación de pisos y cerámicos",
            description = "Colocación profesional de pisos cerámicos, porcelanato y mayólicas. Incluye nivelación, pegado y fragua. Acabado perfecto garantizado.",
            category = "Albañilería",
            price = 45.0,
            imageUrl = null,
            rating = 4.6,
            reviewCount = 145,
            provider = mockProviders[2]
        ),
        ServiceItem(
            id = "svc_014",
            title = "Construcción de techos y coberturas",
            description = "Construcción de techos aligerados, losas y coberturas. Incluye encofrado, fierros y vaciado de concreto. Trabajo certificado con garantía.",
            category = "Albañilería",
            price = 120.0,
            imageUrl = null,
            rating = 4.7,
            reviewCount = 65,
            provider = mockProviders[2]
        ),
        ServiceItem(
            id = "svc_015",
            title = "Reparación de grietas y fisuras",
            description = "Sellado profesional de grietas en paredes y techos. Aplicamos técnicas especializadas para evitar reaparición. Garantía de 6 meses.",
            category = "Albañilería",
            price = 32.0,
            imageUrl = null,
            rating = 4.2,
            reviewCount = 96,
            provider = mockProviders[2]
        )
    )

    private val mockLocations = mutableListOf(
        Location(
            id = "loc_001",
            userId = "user_001",
            label = "Casa",
            address = "21B Av Morelli, San Borja, Lima",
            latitude = -12.0897,
            longitude = -77.0282,
            isDefault = true
        ),
        Location(
            id = "loc_002",
            userId = "user_001",
            label = "Empresa",
            address = "1A Javier Prado, San Isidro, Lima",
            latitude = -12.0897,
            longitude = -77.0282,
            isDefault = false
        )
    )

    private val mockPaymentMethods = mutableListOf(
        PaymentMethod(
            id = "pm_001",
            type = PaymentType.CASH,
            last4 = null,
            expiryMonth = null,
            expiryYear = null,
            cardholderName = null,
            label = "",
            isDefault = false,
        ),
        PaymentMethod(
            id = "pm_002",
            type = PaymentType.PAYPAL,
            last4 = null,
            expiryMonth = null,
            expiryYear = null,
            cardholderName = null,
            label = "",
            isDefault = false,
        ),
        PaymentMethod(
            id = "pm_003",
            type = PaymentType.GOOGLE_PAY,
            last4 = null,
            expiryMonth = null,
            expiryYear = null,
            cardholderName = null,
            label = "",
            isDefault = false,
        ),
        PaymentMethod(
            id = "pm_004",
            type = PaymentType.APPLE_PAY,
            last4 = null,
            expiryMonth = null,
            expiryYear = null,
            cardholderName = null,
            label = "",
            isDefault = false,
        ),
        PaymentMethod(
            id = "pm_005",
            type = PaymentType.CARD,
            last4 = "2259",
            expiryMonth = 12,
            expiryYear = 2026,
            cardholderName = "Ricardo Morales",
            label = "",
            isDefault = false,
        )
    )

    private val mockBookings = mutableListOf(
        Booking(
            id = "bk_001",
            serviceId = "svc_001",
            serviceTitle = "Limpieza de sala",
            serviceImage = null,
            servicePrice = 50.0,
            providerName = "Maria Lopez",
            providerPhoto = null,
            date = "Lunes, 25 de octubre, 2025",
            time = "11:00 AM",
            timeEnd = "12:00",
            location = "221B Morelli, San Borja",
            status = BookingStatus.PENDING,
            total = 50.0,
            discount = 0.0,
            paymentMethod = "Efectivo",
            createdAt = "2025-10-20T10:00:00Z",
            hasReview = false
        ),
        Booking(
            id = "bk_002",
            serviceId = "svc_002",
            serviceTitle = "Arreglo de tubería",
            serviceImage = null,
            servicePrice = 500.0,
            providerName = "Carlos Mendoza",
            providerPhoto = null,
            date = "Martes, 24 de octubre, 2025",
            time = "11:30 AM",
            timeEnd = "12:30",
            location = "Av. Principal 123",
            status = BookingStatus.COMPLETED,
            total = 500.0,
            discount = 0.0,
            paymentMethod = "Efectivo",
            createdAt = "2025-10-15T08:00:00Z",
            hasReview = true
        ),
        Booking(
            id = "bk_003",
            serviceId = "svc_003",
            serviceTitle = "Mantenimiento de puerta principal",
            serviceImage = null,
            servicePrice = 300.0,
            providerName = "Juan Rodriguez",
            providerPhoto = null,
            date = "Martes, 24 de octubre, 2025",
            time = "11:30 AM",
            timeEnd = "12:30",
            location = "Calle Secundaria 456",
            status = BookingStatus.CANCELLED,
            total = 300.0,
            discount = 0.0,
            paymentMethod = "Efectivo",
            createdAt = "2025-10-10T12:00:00Z",
            hasReview = false
        )
    )

    suspend fun getCategories(): Result<List<Category>> {
        delay(300)
        return Result.success(mockCategories)
    }

    suspend fun getServices(category: String? = null, search: String? = null): Result<List<ServiceItem>> {
        delay(500)
        var filtered = mockServices

        if (!category.isNullOrBlank()) {
            val normalizedCategory = category.normalize()
            filtered = filtered.filter { it.category.normalize() == normalizedCategory }
        }

        if (!search.isNullOrBlank()) {
            filtered = filtered.filter {
                it.title.contains(search, ignoreCase = true) ||
                it.description.contains(search, ignoreCase = true) ||
                it.category.contains(search, ignoreCase = true)
            }
        }

        return Result.success(filtered)
    }

    suspend fun getServiceDetail(serviceId: String): Result<ServiceItem> {
        delay(300)
        val service = mockServices.find { it.id == serviceId }
        return if (service != null) {
            Result.success(service)
        } else {
            Result.failure(Exception("Service not found"))
        }
    }

    suspend fun getFeaturedWorkers(): Result<List<Provider>> {
        delay(300)
        return Result.success(mockProviders)
    }

    suspend fun getProviderDetail(providerId: String): Result<Provider> {
        delay(300)
        val provider = mockProviders.find { it.id == providerId }
        return if (provider != null) {
            Result.success(provider)
        } else {
            Result.failure(Exception("Provider not found"))
        }
    }

    suspend fun getProviderServices(providerId: String): Result<List<ServiceItem>> {
        delay(400)
        val services = mockServices.filter { it.provider.id == providerId }
        return Result.success(services)
    }

    suspend fun createBooking(
        serviceId: String,
        date: String,
        time: String,
        locationId: String,
        paymentMethodId: String
    ): Result<Booking> {
        delay(700)

        val service = mockServices.find { it.id == serviceId } ?: return Result.failure(Exception("Service not found"))
        val location = mockLocations.find { it.id == locationId } ?: return Result.failure(Exception("Location not found"))
        val payment = mockPaymentMethods.find { it.id == paymentMethodId } ?: return Result.failure(Exception("Payment method not found"))

        // Extraer hora de fin del rango de tiempo (formato: "HH:mm - HH:mm")
        val timeEnd = time.split(" - ").lastOrNull()?.trim() ?: "18:00"

        val newBooking = Booking(
            id = "bk_${System.currentTimeMillis()}",
            serviceId = service.id,
            serviceTitle = service.title,
            serviceImage = service.imageUrl,
            servicePrice = service.price,
            providerName = service.provider.name,
            providerPhoto = service.provider.photo,
            date = date,
            time = time,
            timeEnd = timeEnd,
            location = location.address,
            status = BookingStatus.PENDING,
            total = service.price,
            discount = 0.0,
            paymentMethod = when(payment.type) {
                PaymentType.CASH -> "Efectivo"
                PaymentType.PAYPAL -> "PayPal"
                PaymentType.GOOGLE_PAY -> "Google Pay"
                PaymentType.APPLE_PAY -> "Apple Pay"
                PaymentType.CARD -> "Tarjeta **** ${payment.last4}"
            },
            createdAt = java.time.Instant.now().toString(),
            hasReview = false
        )

        mockBookings.add(0, newBooking)
        return Result.success(newBooking)
    }

    suspend fun getMyBookings(): Result<List<Booking>> {
        delay(500)
        return Result.success(mockBookings)
    }

    suspend fun cancelBooking(bookingId: String): Result<Booking> {
        delay(500)
        val index = mockBookings.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            val updated = mockBookings[index].copy(status = BookingStatus.CANCELLED)
            mockBookings[index] = updated
            return Result.success(updated)
        }
        return Result.failure(Exception("Booking not found"))
    }

    suspend fun getBookingById(bookingId: String): Result<Booking> {
        delay(300)
        val booking = mockBookings.find { it.id == bookingId }
        return if (booking != null) {
            Result.success(booking)
        } else {
            Result.failure(Exception("Booking not found"))
        }
    }

    suspend fun submitReview(bookingId: String, rating: Int, comment: String): Result<Boolean> {
        delay(700)
        val bookingIndex = mockBookings.indexOfFirst { it.id == bookingId }
        if (bookingIndex == -1) {
            return Result.failure(Exception("Booking not found"))
        }

        // En un app real, aquí se enviaría la reseña al backend
        // Por ahora solo simulamos el éxito
        return Result.success(true)
    }

    suspend fun getLocations(): Result<List<Location>> {
        delay(300)
        return Result.success(mockLocations)
    }

    suspend fun createLocation(label: String, address: String): Result<Location> {
        delay(500)
        val newLocation = Location(
            id = "loc_${System.currentTimeMillis()}",
            userId = "user_001",
            label = label,
            address = address,
            latitude = null,
            longitude = null,
            isDefault = mockLocations.isEmpty()
        )
        mockLocations.add(newLocation)
        return Result.success(newLocation)
    }

    suspend fun deleteLocation(locationId: String): Result<Unit> {
        delay(300)
        mockLocations.removeAll { it.id == locationId }
        return Result.success(Unit)
    }

    suspend fun getPaymentMethods(): Result<List<PaymentMethod>> {
        delay(300)
        return Result.success(mockPaymentMethods)
    }

    suspend fun addPaymentMethod(
        type: PaymentType,
        cardNumber: String?,
        expiryMonth: Int?,
        expiryYear: Int?,
        cardholderName: String?
    ): Result<PaymentMethod> {
        delay(500)

        val newPayment = PaymentMethod(
            id = "pm_${System.currentTimeMillis()}",
            type = type,
            last4 = cardNumber?.takeLast(4),
            expiryMonth = expiryMonth,
            expiryYear = expiryYear,
            cardholderName = cardholderName,
            label = "",
            isDefault = false,
        )
        mockPaymentMethods.add(newPayment)
        return Result.success(newPayment)
    }

    suspend fun createReview(
        bookingId: String,
        serviceId: String,
        providerId: String,
        rating: Int,
        comment: String,
        aspects: List<String>,
        wouldRecommend: Boolean
    ): Result<ClientReview> {
        delay(500)

        val newReview = ClientReview(
            id = "rev_${System.currentTimeMillis()}",
            bookingId = bookingId,
            serviceId = serviceId,
            providerId = providerId,
            rating = rating,
            comment = comment,
            aspects = aspects,
            wouldRecommend = wouldRecommend,
            createdAt = java.time.Instant.now().toString()
        )

        return Result.success(newReview)
    }
}
