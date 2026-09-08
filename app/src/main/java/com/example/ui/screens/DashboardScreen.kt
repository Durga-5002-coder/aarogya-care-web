package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HospitalInfo
import com.example.data.model.UserProfile
import com.example.ui.components.AadhaarCardView
import com.example.ui.components.OpTicketCard
import com.example.ui.theme.MedicalAccentAmber
import com.example.ui.theme.MedicalAccentCoral
import com.example.ui.theme.MedicalBlueDark
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.VolumeUp
import com.example.data.localization.AppLanguage
import com.example.data.localization.LocalizationManager
import com.example.data.localization.StringKey
import com.example.ui.components.AudioReadButton
import com.example.ui.components.VoiceCompanionBanner
import com.example.ui.theme.MedicalBluePrimary
import com.example.ui.theme.MedicalTealDark
import com.example.ui.theme.MedicalTealPrimary
import com.example.ui.viewmodel.DashboardViewModel
import com.example.ui.viewmodel.VoiceAssistantViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel,
    onNavigateToChat: () -> Unit,
    onNavigateToScanner: () -> Unit,
    onNavigateToDoctorSummary: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToMedications: () -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {},
    voiceAssistantViewModel: VoiceAssistantViewModel? = null
) {
    val context = LocalContext.current
    val currentUser by dashboardViewModel.currentUser.collectAsState()
    val opRegistrations by dashboardViewModel.opRegistrations.collectAsState()
    val uiState by dashboardViewModel.uiState.collectAsState()

    val currentLanguage by (voiceAssistantViewModel?.currentLanguage ?: MutableStateFlow(AppLanguage.TELUGU)).collectAsState()
    val isSpeaking by (voiceAssistantViewModel?.isSpeaking ?: MutableStateFlow(false)).collectAsState()
    val currentUtteranceId by (voiceAssistantViewModel?.currentUtteranceId ?: MutableStateFlow<String?>(null)).collectAsState()

    val user = currentUser

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // App Bar & Welcome
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MedicalTealDark,
                                MedicalTealPrimary
                            )
                        )
                    )
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "AarogyaCare OP Portal",
                                fontSize = 13.sp,
                                color = Color(0xFFCCFBF1),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = user?.let { "Namaste, ${it.fullName}" } ?: "Namaste Patient",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Language switcher chip
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.22f),
                                modifier = Modifier
                                    .clickable { voiceAssistantViewModel?.showLanguageDialog(true) }
                                    .testTag("top_bar_language_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = "Language",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = currentLanguage.shortBadge,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Emergency SOS button
                            Surface(
                                shape = CircleShape,
                                color = MedicalAccentCoral,
                                modifier = Modifier
                                    .clickable { dashboardViewModel.showEmergencyDialog(true) }
                                    .testTag("emergency_sos_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Emergency 108",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "SOS 108",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Voice Companion Banner for Illiterate & Multi-language accessibility
        item {
            VoiceCompanionBanner(
                currentLanguage = currentLanguage,
                isSpeaking = isSpeaking,
                onOpenLanguagePicker = { voiceAssistantViewModel?.showLanguageDialog(true) },
                onReadScreenAloud = {
                    voiceAssistantViewModel?.speakScreen("dashboard", currentUser?.fullName)
                },
                onOpenVoiceInput = { voiceAssistantViewModel?.showVoiceInputDialog(true) },
                onStopVoice = { voiceAssistantViewModel?.stopSpeaking() }
            )
        }

        // Section 1: Official Aadhaar Profile Card
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                if (user != null) {
                    AadhaarCardView(user = user)
                }
            }
        }

        // Section: Medication Reminder Banner Card
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigateToMedications() }
                        .testTag("dashboard_meds_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFCCFBF1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Medication,
                                    contentDescription = "Medications",
                                    tint = MedicalTealDark,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Prescription & Med Alarms",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFDCFCE7)
                                    ) {
                                        Text(
                                            text = "ROOM DB",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Scheduled daily alarms & local notifications",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = MedicalTealPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Section 2: Quick Action Triage Modules
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "Smart Medical Services",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Medication Alarms
                    QuickActionCard(
                        title = "Med Alarms",
                        subtitle = "Prescription Reminders",
                        icon = Icons.Default.Alarm,
                        color = Color(0xFF0D9488),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_medications",
                        onClick = onNavigateToMedications
                    )

                    // AI Chatbot
                    QuickActionCard(
                        title = "AI Triage Bot",
                        subtitle = "Symptom check & First Aid",
                        icon = Icons.Default.Chat,
                        color = MedicalTealPrimary,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_ai_bot",
                        onClick = onNavigateToChat
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Report Scanner
                    QuickActionCard(
                        title = "Scan Reports",
                        subtitle = "Lab & Prescription OCR",
                        icon = Icons.Default.DocumentScanner,
                        color = MedicalBluePrimary,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_report_scanner",
                        onClick = onNavigateToScanner
                    )

                    // Doctor Summary
                    QuickActionCard(
                        title = "Doctor Summary",
                        subtitle = "Generate SOAP & Submit OP",
                        icon = Icons.Default.Summarize,
                        color = Color(0xFF7C3AED),
                        modifier = Modifier.weight(1f),
                        testTag = "quick_action_doctor_summary",
                        onClick = onNavigateToDoctorSummary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Health Trends & Analytics Visualizer
                Surface(
                    onClick = onNavigateToAnalytics,
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFE0F2F1),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00897B).copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quick_action_health_analytics")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFF00897B), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Healing,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Historical Vitals & Health Trends",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF004D40)
                                )
                                Text(
                                    text = "BP, Blood Sugar, HbA1c & Clinical graphs",
                                    fontSize = 11.sp,
                                    color = Color(0xFF00695C)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF00897B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Section 3: Active OP Consultation Tickets
        if (opRegistrations.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My OP Consultations (${opRegistrations.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    opRegistrations.forEach { ticket ->
                        val ticketUtteranceId = "op_ticket_${ticket.id}"
                        OpTicketCard(
                            ticket = ticket,
                            isSpeakingThisTicket = isSpeaking && currentUtteranceId == ticketUtteranceId,
                            onSpeakTicket = {
                                val spoken = LocalizationManager.getOpTicketSpokenText(
                                    tokenNumber = ticket.tokenNumber.filter { it.isDigit() }.toIntOrNull() ?: 1,
                                    hospitalName = ticket.hospitalName,
                                    department = ticket.department,
                                    roomNumber = ticket.roomNumber,
                                    estimatedWaitTimeMinutes = 15,
                                    language = currentLanguage
                                )
                                voiceAssistantViewModel?.speak(spoken, ticketUtteranceId)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // Section 4: OP Consultation Registration & Hospital Directory Links
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "OP Consultation Registration",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Hospital networks & Online Registration portals",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                dashboardViewModel.hospitalsList.forEach { hospital ->
                    HospitalListItemCard(
                        hospital = hospital,
                        onBookOp = { dashboardViewModel.openOpBooking(hospital) },
                        onOpenPortal = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hospital.officialOpPortalUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Handled safely
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

    // Modal Dialog: Register OP Consultation for Selected Hospital
    if (uiState.isBookingDialogVisible && uiState.selectedHospitalForBooking != null) {
        val hospital = uiState.selectedHospitalForBooking!!
        var selectedDept by remember { mutableStateOf(hospital.departments.first()) }
        var doctorPreference by remember { mutableStateOf("") }
        var appointmentDate by remember { mutableStateOf("Tomorrow, 09:30 AM") }
        var timeSlot by remember { mutableStateOf("Morning Slot (09:00 AM - 11:30 AM)") }
        var patientSymptoms by remember { mutableStateOf("") }
        var isDeptDropdownExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { dashboardViewModel.dismissOpBooking() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocalHospital, contentDescription = null, tint = MedicalTealPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Register OP Consultation",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = hospital.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MedicalTealDark
                    )
                    Text(
                        text = "${hospital.type} • Fee: ${hospital.opConsultationFee}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Department Dropdown
                    ExposedDropdownMenuBox(
                        expanded = isDeptDropdownExpanded,
                        onExpandedChange = { isDeptDropdownExpanded = !isDeptDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedDept,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select OP Department") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDeptDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isDeptDropdownExpanded,
                            onDismissRequest = { isDeptDropdownExpanded = false }
                        ) {
                            hospital.departments.forEach { dept ->
                                DropdownMenuItem(
                                    text = { Text(dept) },
                                    onClick = {
                                        selectedDept = dept
                                        isDeptDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = appointmentDate,
                        onValueChange = { appointmentDate = it },
                        label = { Text("Date & Timing") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = timeSlot,
                        onValueChange = { timeSlot = it },
                        label = { Text("Time Slot") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = patientSymptoms,
                        onValueChange = { patientSymptoms = it },
                        label = { Text("Describe Symptoms / Health Problem") },
                        placeholder = { Text("e.g. Fever for 2 days, dry cough, knee pain") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        dashboardViewModel.confirmOpBooking(
                            hospital = hospital,
                            department = selectedDept,
                            doctor = doctorPreference,
                            date = appointmentDate,
                            slot = timeSlot,
                            symptoms = patientSymptoms
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary),
                    modifier = Modifier.testTag("confirm_op_booking_btn")
                ) {
                    Text("Generate OP Slip")
                }
            },
            dismissButton = {
                TextButton(onClick = { dashboardViewModel.dismissOpBooking() }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Success OP Ticket Confirmation Dialog
    if (uiState.bookingSuccessTicket != null) {
        val ticket = uiState.bookingSuccessTicket!!
        val ticketUtteranceId = "op_ticket_success_${ticket.id}"
        AlertDialog(
            onDismissRequest = { dashboardViewModel.dismissSuccessTicket() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Healing, contentDescription = null, tint = Color(0xFF16A34A))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("OP Registration Confirmed!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Your Outpatient (OP) slot is registered. Show this token or QR code at the hospital OP counter.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OpTicketCard(
                        ticket = ticket,
                        isSpeakingThisTicket = isSpeaking && currentUtteranceId == ticketUtteranceId,
                        onSpeakTicket = {
                            val spoken = LocalizationManager.getOpTicketSpokenText(
                                tokenNumber = ticket.tokenNumber.filter { it.isDigit() }.toIntOrNull() ?: 1,
                                hospitalName = ticket.hospitalName,
                                department = ticket.department,
                                roomNumber = ticket.roomNumber,
                                estimatedWaitTimeMinutes = 15,
                                language = currentLanguage
                            )
                            voiceAssistantViewModel?.speak(spoken, ticketUtteranceId)
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { dashboardViewModel.dismissSuccessTicket() },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary)
                ) {
                    Text("Done")
                }
            }
        )
    }

    // Emergency SOS Dialog
    if (uiState.isEmergencyDialogVisible) {
        AlertDialog(
            onDismissRequest = { dashboardViewModel.showEmergencyDialog(false) },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = MedicalAccentCoral)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Emergency Services", color = MedicalAccentCoral, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    AudioReadButton(
                        isCurrentlySpeaking = isSpeaking && currentUtteranceId == "emergency_sos_spoken",
                        onSpeak = {
                            val spoken = LocalizationManager.get(StringKey.EMERGENCY_SPOKEN, currentLanguage)
                            voiceAssistantViewModel?.speak(spoken, "emergency_sos_spoken")
                        },
                        onStop = { voiceAssistantViewModel?.stopSpeaking() },
                        label = "వినండి"
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("In case of acute cardiac arrest, stroke, severe trauma, or breathing failure, connect to national helplines immediately:")
                    Text("• National Ambulance: 108 / 112", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("• AIIMS Emergency: 011-26593677", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("• Apollo Emergency: 1066", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                            context.startActivity(intent)
                        } catch (e: Exception) {}
                        dashboardViewModel.showEmergencyDialog(false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalAccentCoral)
                ) {
                    Text("Call 108 Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { dashboardViewModel.showEmergencyDialog(false) }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    testTag: String = "",
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
fun HospitalListItemCard(
    hospital: HospitalInfo,
    onBookOp: () -> Unit,
    onOpenPortal: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hospital_card_${hospital.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hospital.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${hospital.city}, ${hospital.state} • ${hospital.type}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
                ) {
                    Text(
                        text = hospital.opConsultationFee,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OP Hours: ${hospital.opHours}",
                    fontSize = 11.sp,
                    color = MedicalTealDark,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons: Register OP Inside App or Visit Official Web Portal Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBookOp,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(38.dp)
                        .testTag("book_op_${hospital.id}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Register OP Slot", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onOpenPortal,
                    modifier = Modifier
                        .weight(0.9f)
                        .height(38.dp)
                        .testTag("open_portal_${hospital.id}"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Portal Link", fontSize = 12.sp)
                }
            }
        }
    }
}
