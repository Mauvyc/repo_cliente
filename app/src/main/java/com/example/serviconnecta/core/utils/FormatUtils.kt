package com.example.serviconnecta.core.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
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

object DateTimeUtils {

    /**
     * Verifica si una fecha y hora programada ya ha pasado.
     *
     * @param scheduledDate Fecha en formato "YYYY-MM-DD"
     * @param endTime Hora de finalización en formato "HH:mm"
     * @return true si la fecha/hora ya pasó, false en caso contrario
     */
    fun hasServiceDateTimePassed(scheduledDate: String, endTime: String): Boolean {
        return try {
            // Formato esperado: "2025-12-06" y "19:00"
            val dateTimeString = "$scheduledDate $endTime"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val scheduledDateTime = dateFormat.parse(dateTimeString)

            if (scheduledDateTime != null) {
                val currentDateTime = Calendar.getInstance().time
                currentDateTime.after(scheduledDateTime)
            } else {
                false
            }
        } catch (e: Exception) {
            android.util.Log.e("DateTimeUtils", "Error parsing date/time: ${e.message}")
            false
        }
    }
}
