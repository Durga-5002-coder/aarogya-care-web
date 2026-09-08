package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.LocalizationManager
import com.example.data.localization.StringKey
import com.example.ui.components.AadhaarCardView
import com.example.ui.components.AudioReadButton
import com.example.ui.theme.MedicalTealDark
import com.example.ui.theme.MedicalTealLight
import com.example.ui.theme.MedicalTealPrimary
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.VoiceAssistantViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit,
    voiceAssistantViewModel: VoiceAssistantViewModel? = null
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val user = currentUser

    val currentLanguage by (voiceAssistantViewModel?.currentLanguage ?: MutableStateFlow(AppLanguage.TELUGU)).collectAsState()
    val isSpeaking by (voiceAssistantViewModel?.isSpeaking ?: MutableStateFlow(false)).collectAsState()
    val currentUtteranceId by (voiceAssistantViewModel?.currentUtteranceId ?: MutableStateFlow<String?>(null)).collectAsState()
    val isVoiceGuideEnabled by (voiceAssistantViewModel?.isVoiceGuideEnabled ?: MutableStateFlow(true)).collectAsState()
    val speechRate by (voiceAssistantViewModel?.speechRate ?: MutableStateFlow(0.85f)).collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Health ID & Aadhaar Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Official National Digital Health Mission (ABHA) & UIDAI Registry",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (user != null) {
            item {
                AadhaarCardView(user = user)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Accessibility & Language Support Card for Illiterate & Multi-language Users
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MedicalTealLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = MedicalTealDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "భాష & వాయిస్ సదుపాయం",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MedicalTealDark
                                    )
                                    Text(
                                        text = "Language & Voice Accessibility",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            AudioReadButton(
                                isCurrentlySpeaking = isSpeaking && currentUtteranceId == "profile_lang_spoken",
                                onSpeak = {
                                    val spoken = "${currentLanguage.nativeName} (${currentLanguage.englishName}). " +
                                            LocalizationManager.get(StringKey.APP_NAME, currentLanguage)
                                    voiceAssistantViewModel?.speak(spoken, "profile_lang_spoken")
                                },
                                onStop = { voiceAssistantViewModel?.stopSpeaking() },
                                label = "వినండి"
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Selected Language Display and Change Action
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { voiceAssistantViewModel?.showLanguageDialog(true) }
                                .testTag("profile_change_language_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Current App Language",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${currentLanguage.nativeName} (${currentLanguage.englishName})",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MedicalTealPrimary
                                ) {
                                    Text(
                                        text = "మార్చండి (Change)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Voice Guide Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = MedicalTealPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Auto Voice Guide (ధ్వని సహాయం)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Reads screens & doctor messages automatically",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Switch(
                                checked = isVoiceGuideEnabled,
                                onCheckedChange = { voiceAssistantViewModel?.toggleVoiceGuide(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MedicalTealPrimary),
                                modifier = Modifier.testTag("voice_guide_switch")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Speech Speed Selector (Elders/Illiterate need slow, clear speech)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = MedicalTealPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "మాట వేగం (Speech Speed):",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    0.75f to "నెమ్మదిగా (0.75x)",
                                    0.85f to "సాధారణం (0.85x)",
                                    1.0f to "వేగంగా (1.0x)"
                                ).forEach { (rate, label) ->
                                    val isSelected = Math.abs(speechRate - rate) < 0.05f
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) MedicalTealPrimary else Color(0xFFF1F5F9),
                                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { voiceAssistantViewModel?.setSpeechRate(rate) }
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else Color(0xFF334155),
                                            modifier = Modifier.padding(vertical = 7.dp),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Aadhaar e-KYC Verification Record",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicalTealDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        ProfileDetailRow(label = "Full Name (Aadhaar)", value = user.fullName, icon = Icons.Default.Person)
                        ProfileDetailRow(label = "Age (from Aadhaar)", value = "${user.age} Years (DOB: ${user.dateOfBirth})", icon = Icons.Default.Shield)
                        ProfileDetailRow(label = "Linked Phone", value = user.phoneNumber, icon = Icons.Default.Phone)
                        ProfileDetailRow(label = "Gender", value = user.gender, icon = Icons.Default.Fingerprint)
                        ProfileDetailRow(label = "Permanent Address", value = user.address, icon = Icons.Default.Home)
                        ProfileDetailRow(label = "ABHA Health Number", value = user.abhaId, icon = Icons.Default.Security)
                        ProfileDetailRow(
                            label = "Firebase Auth UID",
                            value = user.firebaseUid ?: "Connected (Secure UIDAI Token)",
                            icon = Icons.Default.Lock
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                OutlinedButton(
                    onClick = {
                        authViewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("logout_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Logout & Switch Aadhaar Account", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ProfileDetailRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(MedicalTealLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MedicalTealDark, modifier = Modifier.size(16.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
