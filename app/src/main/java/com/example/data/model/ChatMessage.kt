package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageSender {
    USER,
    AI_BOT,
    SYSTEM
}

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userAadhaar: String,
    val sender: String, // USER, AI_BOT, SYSTEM
    val text: String,
    val firstAidSteps: String? = null, // JSON or bulleted string of first aid instructions
    val recommendedSpecialist: String? = null, // e.g. "Cardiologist", "General Physician"
    val urgencyLevel: String? = null, // "Low", "Medium", "High (Emergency)"
    val timestamp: Long = System.currentTimeMillis()
)
