package com.example.serviconnecta.feature.client.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentMethodRequestDto(
    val type: String, // "CARD_SIMULATED"
    val label: String,
    @SerialName("card_number")
    val cardNumber: String,
    @SerialName("card_holder_name")
    val cardHolderName: String,
    @SerialName("exp_month")
    val expMonth: Int,
    @SerialName("exp_year")
    val expYear: Int,
    val cvv: String
)