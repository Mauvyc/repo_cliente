package com.example.serviconnecta.core.network

data class StandardResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val errors: List<String>?
)