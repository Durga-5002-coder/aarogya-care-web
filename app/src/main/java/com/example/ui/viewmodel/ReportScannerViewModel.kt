package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MedicalReport
import com.example.data.model.ReportCategory
import com.example.data.model.ReportHealthStatus
import com.example.data.repository.HealthcareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ReportScannerUiState(
    val selectedCategory: ReportCategory = ReportCategory.ALL,
    val searchQuery: String = "",
    val isScanning: Boolean = false,
    val selectedReportForDetail: MedicalReport? = null,
    val isScanSheetOpen: Boolean = false,
    val scanSuccessMessage: String? = null
)

class ReportScannerViewModel(private val repository: HealthcareRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportScannerUiState())
    val uiState: StateFlow<ReportScannerUiState> = _uiState.asStateFlow()

    val currentUser = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getUser(aadhaar) else repository.getLatestUser()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val rawReports = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getReportsForUser(aadhaar) else flowOf(emptyList())
    }

    val filteredReports: StateFlow<List<MedicalReport>> = combine(
        rawReports,
        _uiState
    ) { reports, state ->
        reports.filter { report ->
            val matchesCategory = when (state.selectedCategory) {
                ReportCategory.ALL -> true
                else -> report.category.equals(state.selectedCategory.name, ignoreCase = true)
            }
            val matchesSearch = if (state.searchQuery.isBlank()) true else {
                report.title.contains(state.searchQuery, ignoreCase = true) ||
                        report.hospitalOrLabName.contains(state.searchQuery, ignoreCase = true) ||
                        report.doctorOrTechnician.contains(state.searchQuery, ignoreCase = true) ||
                        report.summary.contains(state.searchQuery, ignoreCase = true)
            }
            matchesCategory && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sampleReportPresets = listOf(
        SampleReportPreset(
            name = "Complete Blood Count (CBC)",
            description = "Apex Diagnostics Lab - Hemoglobin 11.2, Normal WBC, Normal Platelets",
            sampleText = "Apex Diagnostics Laboratory. Patient: Adult Male. Investigation: Complete Blood Count. Hemoglobin: 11.2 g/dL (Normal: 13-17). Total Leucocyte Count: 7,200/cumm. Platelet Count: 2.4 Lakhs/cumm. Neutrophils: 64%, Lymphocytes: 28%. Advice: Nutritional dietary iron and review."
        ),
        SampleReportPreset(
            name = "Lipid Profile & Cholesterol",
            description = "Apollo Clinic - Total Cholesterol 218 mg/dL, Elevated Triglycerides",
            sampleText = "Apollo Diagnostic Services. Lipid Profile Fasting. Serum Total Cholesterol: 218 mg/dL (Desirable: <200). Serum Triglycerides: 178 mg/dL (Normal: <150). HDL Cholesterol: 43 mg/dL. LDL Cholesterol: 140 mg/dL (Elevated). Impression: Borderline Dyslipidemia."
        ),
        SampleReportPreset(
            name = "Digital Chest X-Ray (PA)",
            description = "Fortis Hospital - Normal pulmonary parenchyma and clear angles",
            sampleText = "Fortis Imaging & Radio-Diagnostics. Chest X-Ray Postero-Anterior (PA) View. Trachea is central. Both lung fields appear normal without focal consolidation or pneumothorax. Bilateral CP angles and dome of diaphragm are normal. Heart size within normal limits."
        ),
        SampleReportPreset(
            name = "HbA1c & Fasting Glucose",
            description = "Max Healthcare - Fasting Sugar 132 mg/dL, HbA1c 6.9%",
            sampleText = "Max Hospital Pathology Dept. Diabetes Evaluation Panel. Fasting Blood Glucose: 132 mg/dL (Normal: 70-99 mg/dL). HbA1c Glycated Hemoglobin: 6.9% (Normal: <5.7%, Pre-diabetic/Diabetic range). Doctor Advice: Dietary glycemic control and OP Endocrine consult."
        ),
        SampleReportPreset(
            name = "Orthopedic Knee X-Ray & Rx",
            description = "AIIMS OPD - Mild early osteoarthritic changes",
            sampleText = "AIIMS Orthopedic OPD. Digital X-Ray Right Knee. Joint space mildly reduced in medial compartment. No fracture or dislocation seen. Impression: Grade I Osteoarthritis Right Knee. Advised quadriceps strengthening exercises and hot fermentation."
        )
    )

    fun selectCategory(category: ReportCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun openScanSheet() {
        _uiState.value = _uiState.value.copy(isScanSheetOpen = true)
    }

    fun dismissScanSheet() {
        _uiState.value = _uiState.value.copy(isScanSheetOpen = false)
    }

    fun viewReportDetail(report: MedicalReport) {
        _uiState.value = _uiState.value.copy(selectedReportForDetail = report)
    }

    fun dismissReportDetail() {
        _uiState.value = _uiState.value.copy(selectedReportForDetail = null)
    }

    fun scanAndProcessReport(reportText: String, reportTitleHint: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true, isScanSheetOpen = false)
            try {
                val newReport = repository.analyzeAndSaveScannedReport(
                    userAadhaar = user.aadhaarNumber,
                    reportText = reportText,
                    fileName = reportTitleHint
                )
                _uiState.value = _uiState.value.copy(
                    isScanning = false,
                    selectedReportForDetail = newReport,
                    scanSuccessMessage = "Report analyzed & saved securely to your profile!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isScanning = false,
                    scanSuccessMessage = "Error scanning report: ${e.message}"
                )
            }
        }
    }

    fun deleteReport(id: Long) {
        viewModelScope.launch {
            repository.deleteReport(id)
            if (_uiState.value.selectedReportForDetail?.id == id) {
                _uiState.value = _uiState.value.copy(selectedReportForDetail = null)
            }
        }
    }
}

data class SampleReportPreset(
    val name: String,
    val description: String,
    val sampleText: String
)
