package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DoctorSummaryRecord
import com.example.data.model.HospitalDirectory
import com.example.data.model.HospitalInfo
import com.example.data.model.MedicalReport
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

data class DoctorSummaryUiState(
    val symptomsInput: String = "",
    val selectedHospital: HospitalInfo = HospitalDirectory.hospitals.first(),
    val selectedDepartment: String = HospitalDirectory.hospitals.first().departments.first(),
    val isGenerating: Boolean = false,
    val isSubmitting: Boolean = false,
    val currentSummary: DoctorSummaryRecord? = null,
    val submissionAckToken: String? = null,
    val isSuccessDialogOpen: Boolean = false,
    val errorMessage: String? = null
)

class DoctorSummaryViewModel(private val repository: HealthcareRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorSummaryUiState())
    val uiState: StateFlow<DoctorSummaryUiState> = _uiState.asStateFlow()

    val currentUser = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getUser(aadhaar) else repository.getLatestUser()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val summariesHistory: StateFlow<List<DoctorSummaryRecord>> = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getDoctorSummaries(aadhaar) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userReports: StateFlow<List<MedicalReport>> = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getReportsForUser(aadhaar) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hospitalsList: List<HospitalInfo> = HospitalDirectory.hospitals

    fun updateSymptomsInput(text: String) {
        _uiState.value = _uiState.value.copy(symptomsInput = text)
    }

    fun selectHospital(hospital: HospitalInfo) {
        _uiState.value = _uiState.value.copy(
            selectedHospital = hospital,
            selectedDepartment = hospital.departments.firstOrNull() ?: "General Medicine"
        )
    }

    fun selectDepartment(dept: String) {
        _uiState.value = _uiState.value.copy(selectedDepartment = dept)
    }

    fun generateSummary() {
        val user = currentUser.value ?: return
        val symptoms = _uiState.value.symptomsInput.ifBlank {
            "Patient presenting for outpatient clinical evaluation with intermittent fatigue, mild seasonal fever, and cough for 3 days."
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true, errorMessage = null)
            try {
                val record = repository.createDoctorSummary(
                    profile = user,
                    symptomsText = symptoms,
                    targetHospital = _uiState.value.selectedHospital.name,
                    targetDepartment = _uiState.value.selectedDepartment,
                    reports = userReports.value
                )
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    currentSummary = record
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    errorMessage = "Failed to generate doctor summary: ${e.message}"
                )
            }
        }
    }

    fun submitToHospitalOpSystem() {
        val summary = _uiState.value.currentSummary ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            try {
                val result = repository.submitSummaryToHospitalOpSystem(
                    summaryId = summary.id,
                    hospitalName = _uiState.value.selectedHospital.name,
                    department = _uiState.value.selectedDepartment
                )
                result.onSuccess { ack ->
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        submissionAckToken = ack,
                        isSuccessDialogOpen = true,
                        currentSummary = summary.copy(
                            isSubmittedToHospital = true,
                            hospitalAckToken = ack,
                            submissionTimestamp = System.currentTimeMillis()
                        )
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = "Submission failed: ${e.message}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun dismissSuccessDialog() {
        _uiState.value = _uiState.value.copy(isSuccessDialogOpen = false)
    }

    fun selectHistorySummary(summary: DoctorSummaryRecord) {
        _uiState.value = _uiState.value.copy(
            currentSummary = summary,
            symptomsInput = summary.chiefComplaint
        )
    }
}
