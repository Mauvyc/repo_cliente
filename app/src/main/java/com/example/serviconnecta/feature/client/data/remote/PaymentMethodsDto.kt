package com.example.serviconnecta.feature.client.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class PaymentMethodsResponseDto(
    val payment_methods: List<PaymentMethodDto> = emptyList()
)
data class PaymentMethodDto(
    // el backend envía "_id"
    val _id: String? = null,
    val type: String,
    val label: String,
    val last4: String? = null,
    val is_default: Boolean = false
)