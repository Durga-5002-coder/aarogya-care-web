package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.OpRegistration
import kotlinx.coroutines.flow.Flow

@Dao
interface OpRegistrationDao {
    @Query("SELECT * FROM op_registrations WHERE userAadhaar = :aadhaar ORDER BY bookingTimestamp DESC")
    fun getRegistrationsForUser(aadhaar: String): Flow<List<OpRegistration>>

    @Query("SELECT * FROM op_registrations ORDER BY bookingTimestamp DESC")
    fun getAllRegistrations(): Flow<List<OpRegistration>>

    @Query("SELECT * FROM op_registrations WHERE id = :id LIMIT 1")
    suspend fun getRegistrationById(id: Long): OpRegistration?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: OpRegistration): Long

    @Update
    suspend fun updateRegistration(registration: OpRegistration)

    @Query("DELETE FROM op_registrations WHERE id = :id")
    suspend fun deleteRegistrationById(id: Long)
}
