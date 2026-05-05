package com.raktavahini.app.data.local

import androidx.room.*
import com.raktavahini.app.data.model.DonationLog
import kotlinx.coroutines.flow.Flow

@Dao
interface DonationLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DonationLog)

    @Query("SELECT * FROM donation_logs WHERE donorId = :donorId ORDER BY donationDate DESC")
    fun getLogsForDonor(donorId: Int): Flow<List<DonationLog>>

    @Query("SELECT * FROM donation_logs ORDER BY donationDate DESC")
    fun getAllLogs(): Flow<List<DonationLog>>
}
