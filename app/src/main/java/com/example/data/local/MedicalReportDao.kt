package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MedicalReport
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalReportDao {
    @Query("SELECT * FROM medical_reports WHERE userAadhaar = :aadhaar ORDER BY timestamp DESC")
    fun getReportsForUser(aadhaar: String): Flow<List<MedicalReport>>

    @Query("SELECT * FROM medical_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<MedicalReport>>

    @Query("SELECT * FROM medical_reports WHERE id = :id LIMIT 1")
    suspend fun getReportById(id: Long): MedicalReport?

    @Query("SELECT * FROM medical_reports WHERE userAadhaar = :aadhaar AND category = :category ORDER BY timestamp DESC")
    fun getReportsByCategory(aadhaar: String, category: String): Flow<List<MedicalReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: MedicalReport): Long

    @Update
    suspend fun updateReport(report: MedicalReport)

    @Delete
    suspend fun deleteReport(report: MedicalReport)

    @Query("DELETE FROM medical_reports WHERE id = :id")
    suspend fun deleteReportById(id: Long)
}
