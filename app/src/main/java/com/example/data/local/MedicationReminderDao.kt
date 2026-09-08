package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MedicationReminder
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationReminderDao {
    @Query("SELECT * FROM medication_reminders WHERE userAadhaar = :aadhaar ORDER BY timeHour ASC, timeMinute ASC")
    fun getRemindersForUser(aadhaar: String): Flow<List<MedicationReminder>>

    @Query("SELECT * FROM medication_reminders WHERE userAadhaar = :aadhaar ORDER BY timeHour ASC, timeMinute ASC")
    suspend fun getRemindersForUserSync(aadhaar: String): List<MedicationReminder>

    @Query("SELECT * FROM medication_reminders WHERE isActive = 1")
    suspend fun getAllActiveReminders(): List<MedicationReminder>

    @Query("SELECT * FROM medication_reminders WHERE id = :id")
    suspend fun getReminderById(id: Long): MedicationReminder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: MedicationReminder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<MedicationReminder>)

    @Update
    suspend fun updateReminder(reminder: MedicationReminder)

    @Delete
    suspend fun deleteReminder(reminder: MedicationReminder)

    @Query("UPDATE medication_reminders SET isActive = :isActive WHERE id = :id")
    suspend fun updateReminderActiveStatus(id: Long, isActive: Boolean)

    @Query("UPDATE medication_reminders SET lastTakenDate = :dateStr, streakDays = :streakDays WHERE id = :id")
    suspend fun markReminderTaken(id: Long, dateStr: String, streakDays: Int)

    @Query("DELETE FROM medication_reminders WHERE userAadhaar = :aadhaar")
    suspend fun deleteAllRemindersForUser(aadhaar: String)
}
