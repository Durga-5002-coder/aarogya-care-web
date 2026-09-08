package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_reminders")
data class MedicationReminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userAadhaar: String,
    val medicineName: String,
    val dosage: String,             // e.g. "1 Tablet", "500 mg", "5 ml syrup"
    val instruction: String,        // e.g. "After Breakfast", "Before Food", "At Bedtime"
    val timeHour: Int,              // 0-23
    val timeMinute: Int,            // 0-59
    val category: String,           // "Diabetes", "Blood Pressure", "Antibiotic", "Pain Relief", "Vitamin/Supplement", "Cardiac", "General"
    val isActive: Boolean = true,
    val lastTakenDate: String = "", // e.g. "2026-08-23" to track today's completion
    val streakDays: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Formats the time into readable 12-hour AM/PM string, e.g. "08:30 AM"
     */
    fun formattedTime(): String {
        val hour12 = when {
            timeHour == 0 -> 12
            timeHour > 12 -> timeHour - 12
            else -> timeHour
        }
        val amPm = if (timeHour >= 12) "PM" else "AM"
        val minStr = if (timeMinute < 10) "0$timeMinute" else "$timeMinute"
        val hourStr = if (hour12 < 10) "0$hour12" else "$hour12"
        return "$hourStr:$minStr $amPm"
    }

    /**
     * Identifies the time slot group: Morning, Afternoon, Evening, Night
     */
    fun getTimeSlot(): String {
        return when (timeHour) {
            in 5..11 -> "Morning"
            in 12..16 -> "Afternoon"
            in 17..20 -> "Evening"
            else -> "Night / Bedtime"
        }
    }
}
