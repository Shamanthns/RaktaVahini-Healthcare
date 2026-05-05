package com.raktavahini.app.data.repository

import com.raktavahini.app.data.local.DonationLogDao
import com.raktavahini.app.data.local.DonorDao
import com.raktavahini.app.data.model.DonationLog
import com.raktavahini.app.data.model.Donor
import kotlinx.coroutines.flow.Flow

class RaktaVahiniRepository(
    private val donorDao: DonorDao,
    private val donationLogDao: DonationLogDao
) {

    fun getCurrentUser(): Flow<Donor?> = donorDao.getCurrentUser()

    suspend fun getCurrentUserOnce(): Donor? = donorDao.getCurrentUserOnce()

    suspend fun registerUser(donor: Donor): Long = donorDao.insertDonor(donor)

    suspend fun updateDonor(donor: Donor) = donorDao.updateDonor(donor)

    fun getAllDonors(): Flow<List<Donor>> = donorDao.getAllDonors()

    fun searchDonorsByBloodGroup(bloodGroup: String, excludeId: Int): Flow<List<Donor>> =
        donorDao.searchDonorsByBloodGroup(bloodGroup, excludeId)

    suspend fun updateAvailability(donorId: Int, available: Boolean) =
        donorDao.updateAvailability(donorId, available)

    suspend fun logDonation(log: DonationLog) {
        donationLogDao.insertLog(log)
        donorDao.updateLastDonationDate(log.donorId, log.donationDate)
    }

    fun getLogsForDonor(donorId: Int): Flow<List<DonationLog>> =
        donationLogDao.getLogsForDonor(donorId)

    suspend fun loginUser(phone: String, password: String): Donor? =
        donorDao.loginUser(phone, password)

    suspend fun getDonorByPhone(phone: String): Donor? =
        donorDao.getDonorByPhone(phone)

    suspend fun logout() {
        donorDao.clearCurrentUser()
    }

    suspend fun setCurrentUser(donorId: Int) {
        donorDao.clearCurrentUser()
        donorDao.setCurrentUser(donorId)
    }
}
