package com.raktavahini.app.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.*

object EligibilityUtils {

    fun isEligible(lastDonationDateMillis: Long): Boolean {
        val lastDate = Instant.ofEpochMilli(lastDonationDateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val today = LocalDate.now()
        val daysSince = ChronoUnit.DAYS.between(lastDate, today)
        return daysSince > 90
    }

    fun daysUntilEligible(lastDonationDateMillis: Long): Long {
        val lastDate = Instant.ofEpochMilli(lastDonationDateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val today = LocalDate.now()
        val daysSince = ChronoUnit.DAYS.between(lastDate, today)
        return maxOf(0L, 91L - daysSince)
    }

    fun formatDate(millis: Long): String {
        val date = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    fun localDateToMillis(date: LocalDate): Long {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}

object LocationUtils {
    /**
     * Haversine formula to calculate distance between two lat/lng points in km
     */
    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}

val BLOOD_GROUPS = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
