package com.raktavahini.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donors")
data class Donor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val password: String = "",
    val bloodGroup: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val lastDonationDate: Long,
    val isAvailable: Boolean = true,
    val isCurrentUser: Boolean = false
)
