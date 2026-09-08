package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctor_summaries")
data class DoctorSummaryRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userAadhaar: String,
    val patientName: String,
    val patientAge: Int,
    val patientGender: String,
    val chiefComplaint: String,
    val historyOfPresentIllness: String,
    val pertinentNegativesAndFlags: String,
    val suggestedTriageCategory: String,
    val recommendedQuestionsForDoctor: String,
    val targetHospital: String,
    val targetDepartment: String,
    val isSubmittedToHospital: Boolean = false,
    val hospitalAckToken: String? = null,
    val submissionTimestamp: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
