package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.HospitalDirectory
import com.example.data.model.HospitalInfo
import com.example.data.model.MedicalReport
import com.example.data.model.OpRegistration
import com.example.data.model.UserProfile
import com.example.data.repository.HealthcareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
    val selectedHospitalForBooking: HospitalInfo? = null,
    val isBookingDialogVisible: Boolean = false,
    val bookingSuccessTicket: OpRegistration? = null,
    val isEmergencyDialogVisible: Boolean = false
)

class DashboardViewModel(private val repository: HealthcareRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    val currentUser = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getUser(aadhaar) else repository.getLatestUser()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val opRegistrations: StateFlow<List<OpRegistration>> = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getRegistrationsForUser(aadhaar) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentReports: StateFlow<List<MedicalReport>> = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getReportsForUser(aadhaar) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hospitalsList: List<HospitalInfo> = HospitalDirectory.hospitals

    fun openOpBooking(hospital: HospitalInfo) {
        _uiState.value = _uiState.value.copy(
            selectedHospitalForBooking = hospital,
            isBookingDialogVisible = true,
            bookingSuccessTicket = null
        )
    }

    fun dismissOpBooking() {
        _uiState.value = _uiState.value.copy(
            isBookingDialogVisible = false,
            selectedHospitalForBooking = null
        )
    }

    fun confirmOpBooking(
        hospital: HospitalInfo,
        department: String,
        doctor: String,
        date: String,
        slot: String,
        symptoms: String
    ) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val ticket = repository.registerOpConsultation(
                userProfile = user,
                hospitalName = hospital.name,
                department = department,
                doctorName = doctor.ifBlank { "Consultant Specialist (OP Queue)" },
                appointmentDate = date,
                timeSlot = slot,
                symptoms = symptoms.ifBlank { "General OP Health Consultation" }
            )
            _uiState.value = _uiState.value.copy(
                isBookingDialogVisible = false,
                bookingSuccessTicket = ticket
            )
        }
    }

    fun dismissSuccessTicket() {
        _uiState.value = _uiState.value.copy(bookingSuccessTicket = null)
    }

    fun showEmergencyDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(isEmergencyDialogVisible = show)
    }
}
