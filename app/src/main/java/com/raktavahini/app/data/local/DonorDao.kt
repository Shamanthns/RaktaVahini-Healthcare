package com.raktavahini.app.data.local

import androidx.room.*
import com.raktavahini.app.data.model.Donor
import kotlinx.coroutines.flow.Flow

@Dao
interface DonorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonor(donor: Donor): Long

    @Update
    suspend fun updateDonor(donor: Donor)

    @Delete
    suspend fun deleteDonor(donor: Donor)

    @Query("SELECT * FROM donors WHERE isCurrentUser = 1 LIMIT 1")
    fun getCurrentUser(): Flow<Donor?>

    @Query("SELECT * FROM donors WHERE id = :id")
    suspend fun getDonorById(id: Int): Donor?

    @Query("SELECT * FROM donors WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUserOnce(): Donor?

    @Query("SELECT * FROM donors WHERE phone = :phone AND password = :password LIMIT 1")
    suspend fun loginUser(phone: String, password: String): Donor?

    @Query("SELECT * FROM donors WHERE phone = :phone LIMIT 1")
    suspend fun getDonorByPhone(phone: String): Donor?

    @Query("SELECT * FROM donors WHERE bloodGroup = :bloodGroup AND isAvailable = 1 AND id != :excludeId")
    fun searchDonorsByBloodGroup(bloodGroup: String, excludeId: Int): Flow<List<Donor>>

    @Query("SELECT * FROM donors")
    fun getAllDonors(): Flow<List<Donor>>

    @Query("UPDATE donors SET isAvailable = :available WHERE id = :donorId")
    suspend fun updateAvailability(donorId: Int, available: Boolean)

    @Query("UPDATE donors SET lastDonationDate = :date WHERE id = :donorId")
    suspend fun updateLastDonationDate(donorId: Int, date: Long)

    @Query("UPDATE donors SET isCurrentUser = 0")
    suspend fun clearCurrentUser()

    @Query("UPDATE donors SET isCurrentUser = 1 WHERE id = :donorId")
    suspend fun setCurrentUser(donorId: Int)
}
