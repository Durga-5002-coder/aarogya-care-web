package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DoctorSummaryRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorSummaryDao {
    @Query("SELECT * FROM doctor_summaries WHERE userAadhaar = :aadhaar ORDER BY createdAt DESC")
    fun getSummariesForUser(aadhaar: String): Flow<List<DoctorSummaryRecord>>

    @Query("SELECT * FROM doctor_summaries WHERE id = :id LIMIT 1")
    suspend fun getSummaryById(id: Long): DoctorSummaryRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: DoctorSummaryRecord): Long

    @Update
    suspend fun updateSummary(summary: DoctorSummaryRecord)

    @Query("DELETE FROM doctor_summaries WHERE id = :id")
    suspend fun deleteSummaryById(id: Long)
}
