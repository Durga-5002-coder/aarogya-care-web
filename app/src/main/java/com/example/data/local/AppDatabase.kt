package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.DoctorSummaryRecord
import com.example.data.model.HealthMetricRecord
import com.example.data.model.MedicalReport
import com.example.data.model.MedicationReminder
import com.example.data.model.OpRegistration
import com.example.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        MedicalReport::class,
        OpRegistration::class,
        ChatMessage::class,
        DoctorSummaryRecord::class,
        MedicationReminder::class,
        HealthMetricRecord::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun medicalReportDao(): MedicalReportDao
    abstract fun opRegistrationDao(): OpRegistrationDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun doctorSummaryDao(): DoctorSummaryDao
    abstract fun medicationReminderDao(): MedicationReminderDao
    abstract fun healthMetricDao(): HealthMetricDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aarogya_healthcare_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
