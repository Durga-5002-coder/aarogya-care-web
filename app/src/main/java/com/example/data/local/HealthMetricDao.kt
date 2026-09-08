package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.HealthMetricRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthMetricDao {
    @Query("SELECT * FROM health_metrics WHERE userAadhaar = :aadhaar ORDER BY timestamp ASC")
    fun getAllMetricsForUser(aadhaar: String): Flow<List<HealthMetricRecord>>

    @Query("SELECT * FROM health_metrics WHERE userAadhaar = :aadhaar AND metricType = :type ORDER BY timestamp ASC")
    fun getMetricsByType(aadhaar: String, type: String): Flow<List<HealthMetricRecord>>

    @Query("SELECT * FROM health_metrics WHERE userAadhaar = :aadhaar ORDER BY timestamp ASC")
    suspend fun getAllMetricsForUserSync(aadhaar: String): List<HealthMetricRecord>

    @Query("SELECT * FROM health_metrics WHERE userAadhaar = :aadhaar AND metricType = :type ORDER BY timestamp DESC LIMIT 1")
    fun getLatestMetric(aadhaar: String, type: String): Flow<HealthMetricRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: HealthMetricRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetrics(metrics: List<HealthMetricRecord>)

    @Update
    suspend fun updateMetric(metric: HealthMetricRecord)

    @Delete
    suspend fun deleteMetric(metric: HealthMetricRecord)

    @Query("DELETE FROM health_metrics WHERE userAadhaar = :aadhaar")
    suspend fun deleteAllMetricsForUser(aadhaar: String)
}
