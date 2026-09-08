package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "op_registrations")
data class OpRegistration(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tokenNumber: String, // e.g. "OP-DEL-2026-482"
    val userAadhaar: String,
    val patientName: String,
    val patientAge: Int,
    val patientPhone: String,
    val hospitalName: String,
    val department: String,
    val doctorName: String,
    val appointmentDate: String,
    val timeSlot: String,
    val symptoms: String,
    val clinicalSummaryId: Long? = null,
    val clinicalSummaryText: String? = null,
    val roomNumber: String,
    val status: String = "CONFIRMED", // CONFIRMED, CALLED, COMPLETED
    val bookingTimestamp: Long = System.currentTimeMillis()
)
