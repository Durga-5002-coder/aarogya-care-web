package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AadhaarFirebaseAuthManager
import com.example.data.model.UserProfile
import com.example.data.repository.HealthcareRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Unauthenticated : AuthUiState
    object Loading : AuthUiState
    data class Authenticated(val user: UserProfile) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(private val repository: HealthcareRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    val currentFirebaseUser: StateFlow<FirebaseUser?> = AadhaarFirebaseAuthManager.currentFirebaseUser
    val isFirebaseReady: StateFlow<Boolean> = AadhaarFirebaseAuthManager.isFirebaseReady

    val currentAadhaar = repository.currentUserAadhaar

    val currentUser: StateFlow<UserProfile?> = currentAadhaar.flatMapLatest { aadhaar ->
        if (aadhaar != null) repository.getUser(aadhaar) else repository.getLatestUser()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        viewModelScope.launch {
            currentUser.collect { user ->
                if (user != null) {
                    _authState.value = AuthUiState.Authenticated(user)
                } else {
                    _authState.value = AuthUiState.Unauthenticated
                }
            }
        }
    }

    fun loginWithAadhaar(aadhaarNumber: String, otp: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = repository.authenticateWithAadhaar(aadhaarNumber, otp)
            result.onSuccess { user ->
                _authState.value = AuthUiState.Authenticated(user)
            }.onFailure { err ->
                _authState.value = AuthUiState.Error(err.message ?: "Authentication failed")
            }
        }
    }

    fun registerNewUser(
        fullName: String,
        age: Int,
        gender: String,
        phone: String,
        aadhaarInput: String,
        address: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = repository.registerNewUser(fullName, age, gender, phone, aadhaarInput, address)
            result.onSuccess { user ->
                _authState.value = AuthUiState.Authenticated(user)
            }.onFailure { err ->
                _authState.value = AuthUiState.Error(err.message ?: "Registration failed")
            }
        }
    }

    fun switchOrSelectUser(user: UserProfile) {
        repository.setCurrentUser(user.aadhaarNumber)
        _authState.value = AuthUiState.Authenticated(user)
    }

    fun logout() {
        repository.logoutUser()
        _authState.value = AuthUiState.Unauthenticated
    }
}
