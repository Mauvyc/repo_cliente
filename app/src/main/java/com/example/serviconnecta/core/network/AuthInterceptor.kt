package com.example.serviconnecta.core.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val accessTokenProvider: () -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = accessTokenProvider()

        // Si no hay token, seguimos sin modificar el request
        if (token.isNullOrBlank()) {
            return chain.proceed(original)
        }

        val newRequest = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}