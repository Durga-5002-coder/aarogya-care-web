package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profiles WHERE aadhaarNumber = :aadhaar LIMIT 1")
    fun getUser(aadhaar: String): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE aadhaarNumber = :aadhaar LIMIT 1")
    suspend fun getUserByAadhaarSync(aadhaar: String): UserProfile?

    @Query("SELECT * FROM user_profiles ORDER BY linkedAt DESC LIMIT 1")
    fun getLatestUser(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles ORDER BY linkedAt DESC")
    fun getAllUsers(): Flow<List<UserProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile)

    @Update
    suspend fun updateUser(user: UserProfile)

    @Query("DELETE FROM user_profiles WHERE aadhaarNumber = :aadhaar")
    suspend fun deleteUser(aadhaar: String)
}
