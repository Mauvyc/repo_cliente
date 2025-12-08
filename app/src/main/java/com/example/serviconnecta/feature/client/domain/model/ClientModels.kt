package com.example.serviconnecta.feature.client.domain.model

data class ServiceComment(
    val id: String,
    val authorName: String,
    val rating: Double,
    val comment: String,
    val createdAt: String
)
data class Category(
    val id: String,
    val name: String,
    val iconName: String?,
    val slug: String
)

data class ServiceItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val imageUrl: String?,
    val rating: Double,
    val reviewCount: Int,
    val provider: Provider,
    val comments: List<ServiceComment> = emptyList()
)

data class Provider(
    val id: String,
    val name: String,
    val photo: String?,
    val specialty: String,
    val rating: Double,
    val reviewCount: Int = 0,
    val yearsExperience: Int? = null
)

data class Booking(
    val id: String,
    val serviceId: String,
    val serviceTitle: String,
    val serviceImage: String?,
    val servicePrice: Double,
    val providerName: String,
    val providerPhoto: String?,
    val date: String,
    val time: String,
    val location: String,
    val status: BookingStatus,
    val total: Double,
    val discount: Double,
    val paymentMethod: String,
    val createdAt: String
)

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

data class Location(
    val id: String,
    val userId: String,
    val label: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?,
    val isDefault: Boolean
)

data class PaymentMethod(
    val id: String,
    val type: PaymentType,
    val label: String,
    val last4: String?,
    val isDefault: Boolean,
    val expiryMonth: Int? = null,
    val expiryYear: Int? = null,
    val cardholderName: String? = null
)

enum class PaymentType {
    CASH,
    PAYPAL,
    GOOGLE_PAY,
    APPLE_PAY,
    CARD
}

data class ClientReview(
    val id: String,
    val bookingId: String,
    val serviceId: String,
    val providerId: String,
    val rating: Int,
    val comment: String,
    val aspects: List<String>,
    val wouldRecommend: Boolean,
    val createdAt: String
)

/**
 * Modelo de dominio para el detalle completo de un proveedor.
 * Contiene toda la información necesaria para mostrar el perfil público del proveedor.
 */
data class ProviderDetail(
    val provider: Provider,
    val services: List<ServiceItem>,
    val description: String? = null,
    val isVerified: Boolean = false
)
