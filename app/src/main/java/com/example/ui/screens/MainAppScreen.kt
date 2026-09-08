package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.LocalizationManager
import com.example.data.localization.StringKey
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.components.VoiceInputDialog
import com.example.ui.theme.MedicalTealDark
import com.example.ui.theme.MedicalTealPrimary
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.ChatbotViewModel
import com.example.ui.viewmodel.DashboardViewModel
import com.example.ui.viewmodel.DoctorSummaryViewModel
import com.example.ui.viewmodel.HealthAnalyticsViewModel
import com.example.ui.viewmodel.MedicationViewModel
import com.example.ui.viewmodel.ReportScannerViewModel
import com.example.ui.viewmodel.VoiceAssistantViewModel

enum class MainNavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DASHBOARD(
        title = "Home",
        selectedIcon = Icons.Filled.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard,
        testTag = "nav_tab_dashboard"
    ),
    ANALYTICS(
        title = "Trends",
        selectedIcon = Icons.Filled.ShowChart,
        unselectedIcon = Icons.Outlined.ShowChart,
        testTag = "nav_tab_analytics"
    ),
    MEDICATIONS(
        title = "Meds",
        selectedIcon = Icons.Filled.Medication,
        unselectedIcon = Icons.Outlined.Medication,
        testTag = "nav_tab_medications"
    ),
    CHATBOT(
        title = "AI Triage",
        selectedIcon = Icons.Filled.Chat,
        unselectedIcon = Icons.Outlined.Chat,
        testTag = "nav_tab_chatbot"
    ),
    SCANNER(
        title = "Reports",
        selectedIcon = Icons.Filled.DocumentScanner,
        unselectedIcon = Icons.Outlined.DocumentScanner,
        testTag = "nav_tab_scanner"
    ),
    DOCTOR_SUMMARY(
        title = "Summary",
        selectedIcon = Icons.Filled.Summarize,
        unselectedIcon = Icons.Outlined.Summarize,
        testTag = "nav_tab_doctor_summary"
    ),
    PROFILE(
        title = "Aadhaar",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        testTag = "nav_tab_profile"
    )
}

@Composable
fun MainAppScreen(
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel,
    healthAnalyticsViewModel: HealthAnalyticsViewModel,
    medicationViewModel: MedicationViewModel,
    chatbotViewModel: ChatbotViewModel,
    reportScannerViewModel: ReportScannerViewModel,
    doctorSummaryViewModel: DoctorSummaryViewModel,
    voiceAssistantViewModel: VoiceAssistantViewModel? = null,
    initialTab: MainNavTab = MainNavTab.DASHBOARD,
    onLogout: () -> Unit
) {
    var currentTab by remember { mutableStateOf(initialTab) }

    val currentLanguage by (voiceAssistantViewModel?.currentLanguage ?: kotlinx.coroutines.flow.MutableStateFlow(AppLanguage.TELUGU)).collectAsState()
    val isLanguageDialogVisible by (voiceAssistantViewModel?.isLanguageDialogVisible ?: kotlinx.coroutines.flow.MutableStateFlow(false)).collectAsState()
    val isVoiceInputDialogVisible by (voiceAssistantViewModel?.isVoiceInputDialogVisible ?: kotlinx.coroutines.flow.MutableStateFlow(false)).collectAsState()
    val isListening by (voiceAssistantViewModel?.isListening ?: kotlinx.coroutines.flow.MutableStateFlow(false)).collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                MainNavTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(19.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 8.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MedicalTealPrimary,
                            selectedTextColor = MedicalTealDark,
                            indicatorColor = MedicalTealPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainNavTab.DASHBOARD -> DashboardScreen(
                    dashboardViewModel = dashboardViewModel,
                    onNavigateToChat = { currentTab = MainNavTab.CHATBOT },
                    onNavigateToScanner = { currentTab = MainNavTab.SCANNER },
                    onNavigateToDoctorSummary = { currentTab = MainNavTab.DOCTOR_SUMMARY },
                    onNavigateToProfile = { currentTab = MainNavTab.PROFILE },
                    onNavigateToMedications = { currentTab = MainNavTab.MEDICATIONS },
                    onNavigateToAnalytics = { currentTab = MainNavTab.ANALYTICS },
                    voiceAssistantViewModel = voiceAssistantViewModel
                )
                MainNavTab.ANALYTICS -> HealthAnalyticsScreen(
                    analyticsViewModel = healthAnalyticsViewModel,
                    onNavigateToDoctorSummary = { currentTab = MainNavTab.DOCTOR_SUMMARY },
                    onNavigateToReports = { currentTab = MainNavTab.SCANNER }
                )
                MainNavTab.MEDICATIONS -> MedicationReminderScreen(
                    medicationViewModel = medicationViewModel
                )
                MainNavTab.CHATBOT -> ChatbotScreen(
                    chatbotViewModel = chatbotViewModel,
                    onNavigateToReports = { currentTab = MainNavTab.SCANNER },
                    voiceAssistantViewModel = voiceAssistantViewModel
                )
                MainNavTab.SCANNER -> ReportScannerScreen(
                    reportScannerViewModel = reportScannerViewModel
                )
                MainNavTab.DOCTOR_SUMMARY -> DoctorSummaryScreen(
                    doctorSummaryViewModel = doctorSummaryViewModel
                )
                MainNavTab.PROFILE -> ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = onLogout,
                    voiceAssistantViewModel = voiceAssistantViewModel
                )
            }
        }
    }

    // Global Language Picker Dialog
    if (isLanguageDialogVisible && voiceAssistantViewModel != null) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onSelectLanguage = { lang ->
                voiceAssistantViewModel.setLanguage(lang)
            },
            onPreviewAudio = { lang ->
                val sampleText = "${lang.nativeName}. " + LocalizationManager.get(StringKey.APP_NAME, lang)
                voiceAssistantViewModel.speak(sampleText, "lang_preview_${lang.code}")
            },
            onDismiss = {
                voiceAssistantViewModel.showLanguageDialog(false)
            }
        )
    }

    // Global Voice Input & Symptom Shortcut Dialog
    if (isVoiceInputDialogVisible && voiceAssistantViewModel != null) {
        VoiceInputDialog(
            currentLanguage = currentLanguage,
            isListening = isListening,
            onStartListening = {
                voiceAssistantViewModel.startListening(
                    onResult = { transcription ->
                        // Transcription handled
                    }
                )
            },
            onStopListening = { voiceAssistantViewModel.stopListening() },
            onPreviewSymptomAudio = { phrase ->
                voiceAssistantViewModel.speak(phrase, "symptom_preview")
            },
            onSubmitSpokenText = { spokenText ->
                voiceAssistantViewModel.showVoiceInputDialog(false)
                currentTab = MainNavTab.CHATBOT
                chatbotViewModel.sendMessage(spokenText)
            },
            onDismiss = {
                voiceAssistantViewModel.showVoiceInputDialog(false)
            }
        )
    }
}
