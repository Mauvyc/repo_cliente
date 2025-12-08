package com.example.serviconnecta.core.utils

import java.text.NumberFormat
import java.util.Locale

object FormatUtils {

    fun formatPrice(price: Double): String {
        return "S/. %.2f".format(price)
    }

    fun formatRating(rating: Double): String {
        return "%.1f".format(rating)
    }

    fun formatDate(dateString: String): String {
        // Simple formatter - input already formatted from mock data
        return dateString
    }

    fun formatTime(timeString: String): String {
        // Simple formatter - input already formatted from mock data
        return timeString
    }
}
