package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MetricType(
    val displayName: String,
    val unit: String,
    val normalMin: Double,
    val normalMax: Double,
    val normalSecondaryMin: Double? = null,
    val normalSecondaryMax: Double? = null
) {
    BLOOD_PRESSURE("Blood Pressure", "mmHg", 90.0, 120.0, 60.0, 80.0),
    BLOOD_GLUCOSE("Blood Glucose", "mg/dL", 70.0, 99.0, 100.0, 140.0),
    HEART_RATE("Heart Rate", "bpm", 60.0, 100.0),
    OXYGEN_SPO2("Oxygen SpO2", "%", 95.0, 100.0),
    HBA1C("HbA1c Glycated Hemoglobin", "%", 4.0, 5.6),
    CHOLESTEROL("Total Cholesterol", "mg/dL", 125.0, 200.0),
    BODY_WEIGHT("Body Weight", "kg", 50.0, 80.0)
}

@Entity(tableName = "health_metrics")
data class HealthMetricRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userAadhaar: String,
    val metricType: String, // from MetricType.name
    val valuePrimary: Double,
    val valueSecondary: Double? = null, // e.g. Diastolic BP for Blood Pressure
    val unit: String,
    val contextTag: String = "Routine", // e.g. "Fasting", "Post-Meal", "Resting", "Lab Report"
    val notes: String = "",
    val dateString: String, // e.g. "2026-08-22"
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Formats the reading value into a clean clinical string, e.g. "120/80 mmHg" or "98 mg/dL"
     */
    fun formattedReading(): String {
        return if (valueSecondary != null) {
            "${valuePrimary.toInt()}/${valueSecondary.toInt()} $unit"
        } else {
            if (valuePrimary % 1.0 == 0.0) {
                "${valuePrimary.toInt()} $unit"
            } else {
                String.format("%.1f %s", valuePrimary, unit)
            }
        }
    }

    /**
     * Clinical status interpretation
     */
    fun getClinicalStatus(): String {
        return when (metricType) {
            MetricType.BLOOD_PRESSURE.name -> {
                val sys = valuePrimary
                val dia = valueSecondary ?: 80.0
                when {
                    sys < 90 || dia < 60 -> "Low BP"
                    sys <= 120 && dia <= 80 -> "Optimal (Normal)"
                    sys in 121.0..129.0 && dia <= 80 -> "Elevated"
                    sys in 130.0..139.0 || dia in 80.0..89.0 -> "Stage 1 Hypertension"
                    else -> "Stage 2 Hypertension"
                }
            }
            MetricType.BLOOD_GLUCOSE.name -> {
                when {
                    contextTag.contains("Fasting", ignoreCase = true) -> {
                        when {
                            valuePrimary < 70 -> "Hypoglycemia"
                            valuePrimary in 70.0..99.0 -> "Normal Fasting"
                            valuePrimary in 100.0..125.0 -> "Pre-diabetes (Impaired)"
                            else -> "Diabetic Range"
                        }
                    }
                    contextTag.contains("Post", ignoreCase = true) -> {
                        when {
                            valuePrimary < 140 -> "Normal Post-Meal"
                            valuePrimary in 140.0..199.0 -> "Pre-diabetes Range"
                            else -> "Elevated Post-Meal"
                        }
                    }
                    else -> {
                        when {
                            valuePrimary < 140 -> "Normal Random"
                            else -> "Elevated Glucose"
                        }
                    }
                }
            }
            MetricType.HEART_RATE.name -> {
                when {
                    valuePrimary < 60 -> "Bradycardia (Slow)"
                    valuePrimary in 60.0..100.0 -> "Normal Resting Pulse"
                    else -> "Tachycardia (Elevated)"
                }
            }
            MetricType.OXYGEN_SPO2.name -> {
                when {
                    valuePrimary >= 95.0 -> "Normal Saturation"
                    valuePrimary in 90.0..94.0 -> "Mild Hypoxia"
                    else -> "Action Required (<90%)"
                }
            }
            MetricType.HBA1C.name -> {
                when {
                    valuePrimary < 5.7 -> "Normal (<5.7%)"
                    valuePrimary in 5.7..6.4 -> "Prediabetic (5.7 - 6.4%)"
                    else -> "Diabetic (>=6.5%)"
                }
            }
            else -> "Normal Range"
        }
    }
}
