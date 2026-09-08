package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MedicationReminder
import com.example.data.model.UserProfile
import com.example.data.repository.HealthcareRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class MedicationUiState(
    val selectedFilter: String = "All", // "All", "Morning", "Afternoon", "Evening", "Night"
    val isAddDialogOpen: Boolean = false,
    val medicineName: String = "",
    val dosage: String = "1 Tablet",
    val instruction: String = "After Food",
    val timeHour: Int = 8,
    val timeMinute: Int = 30,
    val category: String = "General",
    val isSaving: Boolean = false,
    val showTestToastMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class MedicationViewModel(
    private val repository: HealthcareRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicationUiState())
    val uiState: StateFlow<MedicationUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<UserProfile?> = repository.currentUserAadhaar
        .flatMapLatest { aadhaar ->
            if (aadhaar != null) repository.getUser(aadhaar) else flowOf(null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val reminders: StateFlow<List<MedicationReminder>> = repository.currentUserAadhaar
        .flatMapLatest { aadhaar ->
            if (aadhaar != null) repository.getMedicationReminders(aadhaar) else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setFilter(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun openAddDialog(open: Boolean) {
        if (open) {
            val now = Calendar.getInstance()
            _uiState.update {
                it.copy(
                    isAddDialogOpen = true,
                    medicineName = "",
                    dosage = "1 Tablet",
                    instruction = "After Breakfast",
                    timeHour = now.get(Calendar.HOUR_OF_DAY),
                    timeMinute = if (now.get(Calendar.MINUTE) < 30) 30 else 0,
                    category = "General"
                )
            }
        } else {
            _uiState.update { it.copy(isAddDialogOpen = false) }
        }
    }

    fun updateMedicineName(name: String) {
        _uiState.update { it.copy(medicineName = name) }
    }

    fun updateDosage(dosage: String) {
        _uiState.update { it.copy(dosage = dosage) }
    }

    fun updateInstruction(instruction: String) {
        _uiState.update { it.copy(instruction = instruction) }
    }

    fun updateTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(timeHour = hour, timeMinute = minute) }
    }

    fun updateCategory(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun saveMedicationReminder() {
        val state = _uiState.value
        val aadhaar = repository.currentUserAadhaar.value ?: return
        if (state.medicineName.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val reminder = MedicationReminder(
                userAadhaar = aadhaar,
                medicineName = state.medicineName.trim(),
                dosage = state.dosage.trim(),
                instruction = state.instruction.trim(),
                timeHour = state.timeHour,
                timeMinute = state.timeMinute,
                category = state.category,
                isActive = true,
                streakDays = 0
            )
            repository.addMedicationReminder(reminder)
            _uiState.update {
                it.copy(
                    isSaving = false,
                    isAddDialogOpen = false,
                    showTestToastMessage = "Medication reminder set for ${reminder.formattedTime()}!"
                )
            }
        }
    }

    fun toggleMedicationActive(reminder: MedicationReminder, isActive: Boolean) {
        viewModelScope.launch {
            repository.toggleMedicationActive(reminder, isActive)
        }
    }

    fun markTaken(reminder: MedicationReminder) {
        viewModelScope.launch {
            repository.markMedicationTaken(reminder)
            _uiState.update {
                it.copy(showTestToastMessage = "Great job! ${reminder.medicineName} marked as taken today.")
            }
        }
    }

    fun deleteMedication(reminder: MedicationReminder) {
        viewModelScope.launch {
            repository.deleteMedicationReminder(reminder)
        }
    }

    fun triggerTestNotification(reminder: MedicationReminder) {
        repository.triggerTestNotification(reminder)
        _uiState.update {
            it.copy(showTestToastMessage = "Notification triggered for ${reminder.medicineName}! Check your notification drawer.")
        }
    }

    fun clearToastMessage() {
        _uiState.update { it.copy(showTestToastMessage = null) }
    }
}
