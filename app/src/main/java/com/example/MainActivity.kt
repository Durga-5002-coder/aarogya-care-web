package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.repository.HealthcareRepository
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.MainNavTab
import com.example.ui.screens.MainAppScreen
import com.example.ui.theme.AarogyaCareTheme
import com.example.ui.viewmodel.AuthUiState
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.ChatbotViewModel
import com.example.ui.viewmodel.DashboardViewModel
import com.example.ui.viewmodel.DoctorSummaryViewModel
import com.example.ui.viewmodel.HealthAnalyticsViewModel
import com.example.ui.viewmodel.MedicationViewModel
import com.example.ui.viewmodel.ReportScannerViewModel
import com.example.ui.viewmodel.VoiceAssistantViewModel

class MainActivity : ComponentActivity() {
    private var voiceAssistantViewModel: VoiceAssistantViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = HealthcareRepository(applicationContext)
        val voiceVm = VoiceAssistantViewModel(application)
        voiceAssistantViewModel = voiceVm

        val openScreen = intent.getStringExtra("OPEN_SCREEN")
        val initialTab = if (openScreen == "MEDICATIONS") MainNavTab.MEDICATIONS else MainNavTab.DASHBOARD

        setContent {
            AarogyaCareTheme {
                val authViewModel = remember { AuthViewModel(repository) }
                val dashboardViewModel = remember { DashboardViewModel(repository) }
                val healthAnalyticsViewModel = remember { HealthAnalyticsViewModel(repository) }
                val medicationViewModel = remember { MedicationViewModel(repository) }
                val chatbotViewModel = remember { ChatbotViewModel(repository) }
                val reportScannerViewModel = remember { ReportScannerViewModel(repository) }
                val doctorSummaryViewModel = remember { DoctorSummaryViewModel(repository) }

                val authState by authViewModel.authState.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Crossfade(
                        targetState = authState is AuthUiState.Authenticated,
                        label = "auth_crossfade"
                    ) { isAuthenticated ->
                        if (isAuthenticated) {
                            MainAppScreen(
                                authViewModel = authViewModel,
                                dashboardViewModel = dashboardViewModel,
                                healthAnalyticsViewModel = healthAnalyticsViewModel,
                                medicationViewModel = medicationViewModel,
                                chatbotViewModel = chatbotViewModel,
                                reportScannerViewModel = reportScannerViewModel,
                                doctorSummaryViewModel = doctorSummaryViewModel,
                                voiceAssistantViewModel = voiceVm,
                                initialTab = initialTab,
                                onLogout = {
                                    authViewModel.logout()
                                }
                            )
                        } else {
                            AuthScreen(
                                authViewModel = authViewModel,
                                onLoginSuccess = {
                                    // Handled via reactive auth state
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        voiceAssistantViewModel?.cleanup()
        super.onDestroy()
    }
}

