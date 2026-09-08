package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ReportCategory(val displayName: String) {
    ALL("All Reports"),
    LAB_TEST("Lab & Blood Test"),
    PRESCRIPTION("Doctor Prescription"),
    RADIOLOGY("X-Ray & Imaging"),
    OP_SUMMARY("OP Triage Summary"),
    DISCHARGE("Discharge Summary")
}

enum class ReportHealthStatus(val label: String) {
    NORMAL("Normal"),
    ATTENTION("Attention Needed"),
    CRITICAL("Action Required")
}

@Entity(tableName = "medical_reports")
data class MedicalReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userAadhaar: String,
    val title: String,
    val category: String, // from ReportCategory name
    val hospitalOrLabName: String,
    val doctorOrTechnician: String,
    val dateString: String,
    val summary: String,
    val keyMetrics: String, // Formatted metrics or findings
    val doctorAdvice: String,
    val status: String = ReportHealthStatus.NORMAL.name,
    val scanUri: String? = null,
    val rawText: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
