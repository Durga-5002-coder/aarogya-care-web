package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.AppLanguage
import com.example.data.localization.LocalizationManager
import com.example.data.localization.QuickVoiceSymptomsRepository
import com.example.data.localization.StringKey
import com.example.ui.theme.MedicalAccentCoral
import com.example.ui.theme.MedicalTealDark
import com.example.ui.theme.MedicalTealLight
import com.example.ui.theme.MedicalTealPrimary

@Composable
fun VoiceInputDialog(
    currentLanguage: AppLanguage,
    isListening: Boolean,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onPreviewSymptomAudio: (String) -> Unit,
    onSubmitSpokenText: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var spokenQuery by remember { mutableStateOf("") }

    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    AlertDialog(
        onDismissRequest = {
            onStopListening()
            onDismiss()
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(if (isListening) Color(0xFFFFEBEE) else MedicalTealLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = if (isListening) Color(0xFFEF4444) else MedicalTealDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isListening) "వింటున్నాను..." else "మాట్లాడండి / Speak",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isListening) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Voice Assistant (${currentLanguage.nativeName})",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Big Accessible Microphone Button
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .scale(if (isListening) pulseScale else 1.0f)
                        .clip(CircleShape)
                        .background(if (isListening) Color(0xFFEF4444) else MedicalTealPrimary)
                        .clickable {
                            if (isListening) {
                                onStopListening()
                            } else {
                                onStartListening()
                            }
                        }
                        .testTag("voice_dialog_mic_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = "Microphone",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isListening) {
                        LocalizationManager.get(StringKey.VOICE_LISTENING, currentLanguage)
                    } else {
                        LocalizationManager.get(StringKey.VOICE_SPEAK_PROMPT, currentLanguage)
                    },
                    fontSize = 13.sp,
                    fontWeight = if (isListening) FontWeight.Bold else FontWeight.Normal,
                    color = if (isListening) Color(0xFFB91C1C) else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Transcribed / Typed Text Field
                OutlinedTextField(
                    value = spokenQuery,
                    onValueChange = { spokenQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("voice_spoken_input_field"),
                    placeholder = {
                        Text(
                            text = "మీరు మాట్లాడిన మాటలు ఇక్కడ కనిపిస్తాయి / Your spoken words appear here",
                            fontSize = 12.sp
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Illustrated Symptoms Row for Illiterate Patients
                Text(
                    text = "లేదా సమస్యను నొక్కండి (Audio Symptoms):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MedicalTealDark,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(QuickVoiceSymptomsRepository.symptoms) { symptom ->
                        val label = symptom.titles[currentLanguage] ?: symptom.titles[AppLanguage.ENGLISH] ?: ""
                        val spoken = symptom.spokenPhrases[currentLanguage] ?: symptom.spokenPhrases[AppLanguage.ENGLISH] ?: ""

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF1F5F9),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    spokenQuery = spoken
                                    onPreviewSymptomAudio(spoken)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = symptom.iconEmoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Listen",
                                    tint = MedicalTealPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (spokenQuery.isNotBlank()) {
                        onSubmitSpokenText(spokenQuery)
                        onDismiss()
                    }
                },
                enabled = spokenQuery.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MedicalTealDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("submit_voice_query_button")
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "డాక్టర్ కి పంపండి / Submit", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onStopListening()
                    onDismiss()
                }
            ) {
                Text(text = "రద్దు / Cancel")
            }
        }
    )
}
