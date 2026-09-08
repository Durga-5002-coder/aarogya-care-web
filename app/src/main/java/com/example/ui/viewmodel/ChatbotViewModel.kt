package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.MedicalReport
import com.example.data.model.UserProfile
import com.example.data.repository.HealthcareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatbotUiState(
    val isGenerating: Boolean = false,
    val isReportGenerated: Boolean = false,
    val generatedReport: MedicalReport? = null,
    val errorMessage: String? = null
)

class ChatbotViewModel(private val repository: HealthcareRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatbotUiState())
    val uiState: StateFlow<ChatbotUiState> = _uiState.asStateFlow()

    val currentUser = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getUser(aadhaar) else repository.getLatestUser()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val chatMessages: StateFlow<List<ChatMessage>> = repository.currentUserAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getChatMessages(aadhaar) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sampleFirstAidPrompts = listOf(
        "Severe Hot Water Burn on Hand",
        "High Fever with Chills (102°F)",
        "Sprained Ankle & Swelling",
        "Chest Tightness & Shortness of Breath",
        "Asthma Wheezing & Dry Cough",
        "Acute Stomach Cramps & Acidity",
        "Cuts & Bleeding First Aid",
        "Dehydration & Heat Exhaustion"
    )

    fun sendMessage(messageText: String) {
        if (messageText.isBlank()) return
        val user = currentUser.value ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true, errorMessage = null)
            try {
                repository.sendChatMessage(
                    userAadhaar = user.aadhaarNumber,
                    messageText = messageText.trim(),
                    userProfile = user,
                    history = chatMessages.value
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isGenerating = false)
            }
        }
    }

    fun generateTriageMedicalReport() {
        val user = currentUser.value ?: return
        val messages = chatMessages.value
        if (messages.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true)
            try {
                val report = repository.generateAndSaveMedicalTriageReport(user, messages)
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    isReportGenerated = true,
                    generatedReport = report
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    errorMessage = "Failed to generate report: ${e.message}"
                )
            }
        }
    }

    fun dismissReportDialog() {
        _uiState.value = _uiState.value.copy(isReportGenerated = false, generatedReport = null)
    }
}
