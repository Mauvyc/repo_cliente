package com.example.serviconnecta.feature.client.data.remote

import com.example.serviconnecta.feature.client.domain.model.Booking
import com.example.serviconnecta.feature.client.domain.model.BookingStatus
import com.example.serviconnecta.feature.client.domain.model.Location
import com.example.serviconnecta.feature.client.domain.model.PaymentMethod
import com.example.serviconnecta.feature.client.domain.model.PaymentType
import com.example.serviconnecta.feature.client.domain.model.Provider
import com.example.serviconnecta.feature.client.domain.model.ProviderDetail
import com.example.serviconnecta.feature.client.domain.model.ServiceComment
import com.example.serviconnecta.feature.client.domain.model.ServiceItem

// Para el listado de servicios por categoría
fun ServiceDto.toDomainServiceItem(categoryName: String = ""): ServiceItem {
    return ServiceItem(
        id = id,
        title = title,
        description = "", // en el listado no viene descripción
        category = categoryName,
        price = price,
        imageUrl = image_url,
        rating = rating,
        reviewCount = reviews_count,
        provider = Provider(
            id = provider.id,
            name = provider.name,
            photo = provider.avatar_url,
            specialty = provider.profession,
            rating = rating // o un valor fijo si tu backend separa rating proveedor/servicio
        ),
        comments = emptyList() // en el listado no hay comentarios
    )
}

// Para el detalle del servicio
fun ServiceDetailDto.toDomainServiceItem(): ServiceItem {
    return ServiceItem(
        id = id,
        title = title,
        description = description,
        category = category.name,
        price = price,
        imageUrl = image_url,
        rating = rating,
        reviewCount = reviews_count,
        provider = Provider(
            id = provider.id,
            name = provider.name,
            photo = provider.avatar_url,
            specialty = provider.profession,
            rating = rating
        ),
        comments = comments.map { it.toDomainServiceComment() }
    )
}

fun ServiceDetailCommentDto.toDomainServiceComment(): ServiceComment {
    return ServiceComment(
        id = id,
        authorName = author_name,
        rating = rating,
        comment = comment,
        createdAt = created_at
    )
}

fun HomeWorkerDto.toDomainProvider(): Provider {
    return Provider(
        id = id,
        name = name,
        photo = avatar_url,
        specialty = profession,
        rating = rating,
        reviewCount = reviews_count
    )
}

fun ServiceRequestDto.toDomainBooking(): Booking {
    return Booking(
        id = request_id,
        serviceId = service_id,
        serviceTitle = service_title,
        serviceImage = null,         // Backend aún no lo envía
        servicePrice = total,
        providerName = provider_name,
        providerPhoto = null,
        date = scheduled_date,
        time = "${time_range.start} - ${time_range.end}",
        location = "Sin dirección",  // El endpoint no la entrega
        status = when (status) {
            "IN_PROGRESS" -> BookingStatus.IN_PROGRESS
            "COMPLETED" -> BookingStatus.COMPLETED
            "CANCELLED" -> BookingStatus.CANCELLED
            else -> BookingStatus.PENDING
        },
        total = total,
        discount = 0.0,              // El endpoint no lo entrega
        paymentMethod = "Desconocido", // No llega en la respuesta
        createdAt = scheduled_date
    )
}

fun LocationDto.toDomainLocation(): Location =
    Location(
        id = id ?: _id.orEmpty(),       // usa id si viene, si no usa _id
        userId = "",                    // el backend no lo manda, lo dejamos vacío
        label = label,
        address = full_address,
        latitude = latitude,
        longitude = longitude,
        isDefault = is_default
    )

fun PaymentMethodDto.toDomain(): PaymentMethod {
    val finalId = _id
        ?: throw IllegalStateException("PaymentMethod sin _id en la respuesta")

    val typeEnum = when (type) {
        "CASH" -> PaymentType.CASH
        "PAYPAL_SIMULATED" -> PaymentType.PAYPAL
        "GOOGLE_PAY_SIMULATED" -> PaymentType.GOOGLE_PAY
        "APPLE_PAY_SIMULATED" -> PaymentType.APPLE_PAY
        "CARD_SIMULATED" -> PaymentType.CARD
        else -> PaymentType.CARD
    }

    val resolvedLast4 = last4
        ?: Regex("(\\d{4})$")
            .find(label)
            ?.groupValues
            ?.get(1)

    return PaymentMethod(
        id = finalId,
        type = typeEnum,
        label = label,
        last4 = resolvedLast4,
        isDefault = is_default,
        expiryMonth = null,
        expiryYear = null,
        cardholderName = null
    )
}

// Mapeo para ProviderDetailDto a Provider
fun ProviderDetailDto.toDomainProvider(): Provider {
    return Provider(
        id = id,
        name = name,
        photo = avatar_url,
        specialty = profession,
        rating = rating,
        reviewCount = reviews_count,
        yearsExperience = years_experience
    )
}

// Mapeo para ProviderServiceDto a ServiceItem
fun ProviderServiceDto.toDomainServiceItem(provider: Provider): ServiceItem {
    return ServiceItem(
        id = id,
        title = title,
        description = "", // El endpoint de proveedor no incluye descripción completa
        category = category_id, // Solo viene el ID de categoría
        price = price,
        imageUrl = image_url,
        rating = rating,
        reviewCount = reviews_count,
        provider = provider,
        comments = emptyList()
    )
}

// Mapeo completo para ProviderDetailResponseDto a ProviderDetail
fun ProviderDetailResponseDto.toDomainProviderDetail(): ProviderDetail {
    val domainProvider = provider.toDomainProvider()

    return ProviderDetail(
        provider = domainProvider,
        services = services.map { it.toDomainServiceItem(domainProvider) },
        description = null, // El backend puede agregar esto en el futuro
        isVerified = true  // Por defecto true, el backend puede proveerlo
    )
}
