package com.raktavahini.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.raktavahini.app.data.local.RaktaVahiniDatabase
import com.raktavahini.app.data.model.DonationLog
import com.raktavahini.app.data.model.Donor
import com.raktavahini.app.data.repository.RaktaVahiniRepository
import com.raktavahini.app.utils.EligibilityUtils
import com.raktavahini.app.utils.LocationUtils
import com.raktavahini.app.utils.NotificationHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RaktaVahiniViewModel(application: Application) : AndroidViewModel(application) {

    private val db = RaktaVahiniDatabase.getDatabase(application)
    private val repo = RaktaVahiniRepository(db.donorDao(), db.donationLogDao())

    val currentUser: StateFlow<Donor?> = repo.getCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _searchResults = MutableStateFlow<List<Donor>>(emptyList())
    val searchResults: StateFlow<List<Donor>> = _searchResults.asStateFlow()

    private val _selectedBloodGroup = MutableStateFlow("O+")
    val selectedBloodGroup: StateFlow<String> = _selectedBloodGroup.asStateFlow()

    private val _selectedRadius = MutableStateFlow(10)
    val selectedRadius: StateFlow<Int> = _selectedRadius.asStateFlow()

    private val _donationLogs = MutableStateFlow<List<DonationLog>>(emptyList())
    val donationLogs: StateFlow<List<DonationLog>> = _donationLogs.asStateFlow()

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    init {
        NotificationHelper.createNotificationChannel(application)
        viewModelScope.launch {
            currentUser.collect { user ->
                _isRegistered.value = user != null
                user?.let { loadDonationLogs(it.id) }
            }
        }
    }

    fun setSelectedBloodGroup(bloodGroup: String) {
        _selectedBloodGroup.value = bloodGroup
    }

    fun setSelectedRadius(radius: Int) {
        _selectedRadius.value = radius
    }

    fun resetLoginState() { _loginState.value = LoginState.Idle }
    fun resetRegisterState() { _registerState.value = RegisterState.Idle }

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val donor = repo.loginUser(phone.trim(), password.trim())
            if (donor != null) {
                repo.setCurrentUser(donor.id)
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Invalid phone number or password")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _searchResults.value = emptyList()
            _donationLogs.value = emptyList()
        }
    }

    fun registerUser(
        name: String,
        phone: String,
        password: String,
        bloodGroup: String,
        city: String,
        lastDonationDate: LocalDate?
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            // Check if phone already exists
            val existing = repo.getDonorByPhone(phone.trim())
            if (existing != null) {
                _registerState.value = RegisterState.Error("This phone number is already registered. Please login instead.")
                return@launch
            }
            val dateMillis = lastDonationDate?.let {
                it.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            } ?: run {
                LocalDate.now().minusDays(100)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            }
            val donor = Donor(
                name = name,
                phone = phone.trim(),
                password = password.trim(),
                bloodGroup = bloodGroup,
                city = city,
                latitude = 12.9716,
                longitude = 77.5946,
                lastDonationDate = dateMillis,
                isAvailable = true,
                isCurrentUser = true
            )
            repo.registerUser(donor)
            _registerState.value = RegisterState.Success
        }
    }

    fun searchDonors() {
        viewModelScope.launch {
            val user = currentUser.value
            val userLat = user?.latitude ?: 12.9716
            val userLng = user?.longitude ?: 77.5946
            val radiusKm = _selectedRadius.value.toDouble()

            repo.searchDonorsByBloodGroup(_selectedBloodGroup.value, 0)
                .collect { donors ->
                    _searchResults.value = donors.filter { donor ->
                        EligibilityUtils.isEligible(donor.lastDonationDate) &&
                                donor.isAvailable &&
                                LocationUtils.haversineDistance(
                                    userLat, userLng, donor.latitude, donor.longitude
                                ) <= radiusKm
                    }
                }
        }
    }

    fun toggleAvailability() {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repo.updateAvailability(user.id, !user.isAvailable)
        }
    }

    fun logDonation(notes: String, date: LocalDate) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            // Prevent duplicate donation on same date
            val todayMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val alreadyLogged = _donationLogs.value.any { log ->
                val logDate = java.time.Instant.ofEpochMilli(log.donationDate)
                    .atZone(ZoneId.systemDefault()).toLocalDate()
                logDate == date
            }
            if (alreadyLogged) return@launch

            val log = DonationLog(
                donorId = user.id,
                donationDate = todayMillis,
                notes = notes,
                bloodGroup = user.bloodGroup
            )
            repo.logDonation(log)
            NotificationHelper.sendThankYouNotification(getApplication())
            loadDonationLogs(user.id)
        }
    }

    private fun loadDonationLogs(donorId: Int) {
        viewModelScope.launch {
            repo.getLogsForDonor(donorId).collect {
                _donationLogs.value = it
            }
        }
    }

    fun updateProfile(name: String, bloodGroup: String, city: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repo.updateDonor(user.copy(name = name, bloodGroup = bloodGroup, city = city))
        }
    }
}
