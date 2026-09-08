package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val aadhaarNumber: String, // e.g. "5482 9104 3821"
    val fullName: String,                  // e.g. "Durga Reddy Nidrabingi"
    val age: Int,                          // e.g. 28
    val dateOfBirth: String,               // e.g. "14/08/1997"
    val gender: String,                    // e.g. "Male"
    val phoneNumber: String,               // e.g. "+91 98765 43210"
    val address: String,                   // e.g. "Plot 42, Jubilee Hills, Hyderabad, Telangana - 500033"
    val abhaId: String,                    // e.g. "91-4820-9104-3821"
    val photoRes: String = "avatar_user",  // Avatar identifier or URI
    val bloodGroup: String = "O+",
    val isAadhaarVerified: Boolean = true,
    val isPhoneVerified: Boolean = true,
    val firebaseUid: String? = null,
    val linkedAt: Long = System.currentTimeMillis()
)
