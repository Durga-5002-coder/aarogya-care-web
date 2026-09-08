package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DoctorSummaryRecord
import com.example.data.model.HealthMetricRecord
import com.example.data.model.MedicalReport
import com.example.data.model.MedicationReminder
import com.example.data.model.MetricType
import com.example.data.model.OpRegistration
import com.example.data.model.UserProfile
import com.example.data.repository.HealthcareRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HealthAnalyticsUiState(
    val selectedTimeframe: String = "30D", // "7D", "30D", "3M", "6M", "1Y", "All"
    val selectedCategory: String = "Overview", // "Overview", "Blood Pressure", "Blood Glucose", "Vitals & Heart", "Clinical Summaries"
    val selectedPoint: HealthMetricRecord? = null,
    val isAddReadingDialogOpen: Boolean = false,
    val newMetricType: MetricType = MetricType.BLOOD_PRESSURE,
    val newPrimaryValue: String = "120",
    val newSecondaryValue: String = "80",
    val newContextTag: String = "Routine Check",
    val newNotes: String = "",
    val isSaving: Boolean = false,
    val toastMessage: String? = null
)

data class ClinicalSummaryAnalytics(
    val totalSummaries: Int = 0,
    val totalOpVisits: Int = 0,
    val totalLabReports: Int = 0,
    val normalReportsCount: Int = 0,
    val attentionReportsCount: Int = 0,
    val departmentCounts: Map<String, Int> = emptyMap(),
    val urgencyCounts: Map<String, Int> = emptyMap(),
    val avgBpSystolic: Int = 0,
    val avgBpDiastolic: Int = 0,
    val latestBp: HealthMetricRecord? = null,
    val latestGlucose: HealthMetricRecord? = null,
    val latestHbA1c: HealthMetricRecord? = null,
    val latestHeartRate: HealthMetricRecord? = null,
    val latestWeight: HealthMetricRecord? = null,
    val medicationAdherenceRate: Int = 100
)

@OptIn(ExperimentalCoroutinesApi::class)
class HealthAnalyticsViewModel(
    private val repository: HealthcareRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthAnalyticsUiState())
    val uiState: StateFlow<HealthAnalyticsUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<UserProfile?> = repository.currentUserAadhaar
        .flatMapLatest { aadhaar ->
            if (aadhaar != null) repository.getUser(aadhaar) else flowOf(null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val allMetrics: StateFlow<List<HealthMetricRecord>> = repository.currentUserAadhaar
        .flatMapLatest { aadhaar ->
            if (aadhaar != null) repository.getAllHealthMetrics(aadhaar) else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val doctorSummaries: StateFlow<List<DoctorSummaryRecord>> = repository.currentUserAadhaar
        .flatMapLatest { aadhaar ->
            if (aadhaar != null) repository.getDoctorSummaries(aadhaar) else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val medicalReports: StateFlow<List<MedicalReport>> = repository.currentUserAadhaar
        .flatMapLatest { aadhaar ->
            if (aadhaar != null) repository.getReportsForUser(aadhaar) else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val opRegistrations: StateFlow<List<OpRegistration>> = repository.currentUserAadhaar
        .flatMapLatest { aadhaar ->
            if (aadhaar != null) repository.getRegistrationsForUser(aadhaar) else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val medicationReminders: StateFlow<List<MedicationReminder>> = repository.currentUserAadhaar
        .flatMapLatest { aadhaar ->
            if (aadhaar != null) repository.getMedicationReminders(aadhaar) else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val clinicalAnalytics: StateFlow<ClinicalSummaryAnalytics> = combine(
        allMetrics,
        doctorSummaries,
        medicalReports,
        opRegistrations,
        medicationReminders
    ) { metrics, summaries, reports, ops, meds ->
        val deptCounts = mutableMapOf<String, Int>()
        summaries.forEach { s ->
            deptCounts[s.targetDepartment] = (deptCounts[s.targetDepartment] ?: 0) + 1
        }
        ops.forEach { op ->
            deptCounts[op.department] = (deptCounts[op.department] ?: 0) + 1
        }

        val urgencyMap = mutableMapOf<String, Int>()
        summaries.forEach { s ->
            urgencyMap[s.suggestedTriageCategory] = (urgencyMap[s.suggestedTriageCategory] ?: 0) + 1
        }

        val normalReports = reports.count { it.status == "NORMAL" }
        val attentionReports = reports.count { it.status != "NORMAL" }

        val bpList = metrics.filter { it.metricType == MetricType.BLOOD_PRESSURE.name }
        val avgSys = if (bpList.isNotEmpty()) bpList.map { it.valuePrimary }.average().toInt() else 120
        val avgDia = if (bpList.isNotEmpty()) bpList.mapNotNull { it.valueSecondary }.average().toInt() else 80

        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val adherenceRate = if (meds.isNotEmpty()) {
            val takenToday = meds.count { it.lastTakenDate == todayStr }
            ((takenToday.toDouble() / meds.size) * 100).toInt()
        } else 100

        ClinicalSummaryAnalytics(
            totalSummaries = summaries.size,
            totalOpVisits = ops.size,
            totalLabReports = reports.size,
            normalReportsCount = normalReports,
            attentionReportsCount = attentionReports,
            departmentCounts = deptCounts,
            urgencyCounts = urgencyMap,
            avgBpSystolic = avgSys,
            avgBpDiastolic = avgDia,
            latestBp = bpList.lastOrNull(),
            latestGlucose = metrics.lastOrNull { it.metricType == MetricType.BLOOD_GLUCOSE.name },
            latestHbA1c = metrics.lastOrNull { it.metricType == MetricType.HBA1C.name },
            latestHeartRate = metrics.lastOrNull { it.metricType == MetricType.HEART_RATE.name },
            latestWeight = metrics.lastOrNull { it.metricType == MetricType.BODY_WEIGHT.name },
            medicationAdherenceRate = adherenceRate
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ClinicalSummaryAnalytics()
    )

    fun setTimeframe(timeframe: String) {
        _uiState.update { it.copy(selectedTimeframe = timeframe, selectedPoint = null) }
    }

    fun setCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category, selectedPoint = null) }
    }

    fun selectDataPoint(point: HealthMetricRecord?) {
        _uiState.update { it.copy(selectedPoint = point) }
    }

    fun openAddReadingDialog(open: Boolean) {
        if (open) {
            _uiState.update {
                it.copy(
                    isAddReadingDialogOpen = true,
                    newMetricType = MetricType.BLOOD_PRESSURE,
                    newPrimaryValue = "120",
                    newSecondaryValue = "80",
                    newContextTag = "Home Reading",
                    newNotes = ""
                )
            }
        } else {
            _uiState.update { it.copy(isAddReadingDialogOpen = false) }
        }
    }

    fun updateNewMetricType(type: MetricType) {
        val (defaultPrimary, defaultSecondary) = when (type) {
            MetricType.BLOOD_PRESSURE -> Pair("120", "80")
            MetricType.BLOOD_GLUCOSE -> Pair("95", "")
            MetricType.HEART_RATE -> Pair("72", "")
            MetricType.OXYGEN_SPO2 -> Pair("98", "")
            MetricType.HBA1C -> Pair("5.6", "")
            MetricType.CHOLESTEROL -> Pair("180", "")
            MetricType.BODY_WEIGHT -> Pair("70.0", "")
        }
        _uiState.update {
            it.copy(
                newMetricType = type,
                newPrimaryValue = defaultPrimary,
                newSecondaryValue = defaultSecondary
            )
        }
    }

    fun updateNewPrimaryValue(v: String) {
        _uiState.update { it.copy(newPrimaryValue = v) }
    }

    fun updateNewSecondaryValue(v: String) {
        _uiState.update { it.copy(newSecondaryValue = v) }
    }

    fun updateNewContextTag(tag: String) {
        _uiState.update { it.copy(newContextTag = tag) }
    }

    fun updateNewNotes(notes: String) {
        _uiState.update { it.copy(newNotes = notes) }
    }

    fun saveNewHealthReading() {
        val state = _uiState.value
        val aadhaar = repository.currentUserAadhaar.value ?: return

        val pVal = state.newPrimaryValue.toDoubleOrNull() ?: return
        val sVal = if (state.newMetricType == MetricType.BLOOD_PRESSURE) {
            state.newSecondaryValue.toDoubleOrNull() ?: 80.0
        } else null

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val now = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val record = HealthMetricRecord(
                userAadhaar = aadhaar,
                metricType = state.newMetricType.name,
                valuePrimary = pVal,
                valueSecondary = sVal,
                unit = state.newMetricType.unit,
                contextTag = state.newContextTag.trim().ifEmpty { "Manual Entry" },
                notes = state.newNotes.trim(),
                dateString = sdf.format(Date(now)),
                timestamp = now
            )

            repository.addHealthMetric(record)

            _uiState.update {
                it.copy(
                    isSaving = false,
                    isAddReadingDialogOpen = false,
                    toastMessage = "New ${state.newMetricType.displayName} reading saved and plotted!"
                )
            }
        }
    }

    fun deleteMetric(metric: HealthMetricRecord) {
        viewModelScope.launch {
            repository.deleteHealthMetric(metric)
            _uiState.update {
                it.copy(
                    selectedPoint = null,
                    toastMessage = "Reading removed."
                )
            }
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
