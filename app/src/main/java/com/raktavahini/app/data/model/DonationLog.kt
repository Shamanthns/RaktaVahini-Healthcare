package com.raktavahini.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donation_logs")
data class DonationLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val donorId: Int,
    val donationDate: Long, // epoch millis
    val notes: String = "",
    val bloodGroup: String
)
