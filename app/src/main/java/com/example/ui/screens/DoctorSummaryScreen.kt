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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DoctorSummaryRecord
import com.example.data.model.HospitalInfo
import com.example.ui.theme.MedicalBluePrimary
import com.example.ui.theme.MedicalTealDark
import com.example.ui.theme.MedicalTealLight
import com.example.ui.theme.MedicalTealPrimary
import com.example.ui.viewmodel.DoctorSummaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorSummaryScreen(
    doctorSummaryViewModel: DoctorSummaryViewModel
) {
    val uiState by doctorSummaryViewModel.uiState.collectAsState()
    val summariesHistory by doctorSummaryViewModel.summariesHistory.collectAsState()
    val currentUser by doctorSummaryViewModel.currentUser.collectAsState()

    var isHospitalMenuOpen by remember { mutableStateOf(false) }
    var isDeptMenuOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("doctor_summary_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Screen Header
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Clinical OP Doctor Summary",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Converts patient complaints into clinical SOAP format & transmits to hospital OP system",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Intake Section: Describe problem & Select Target Hospital
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MedicalTealLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.MedicalServices, contentDescription = null, tint = MedicalTealDark, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Patient Symptoms & Intake",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.symptomsInput,
                        onValueChange = { doctorSummaryViewModel.updateSymptomsInput(it) },
                        label = { Text("Describe Your Health Problem / Symptoms") },
                        placeholder = { Text("e.g. Sharp pain in lower back radiating to right leg for 5 days, aggravated when sitting...") },
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("symptoms_input_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Hospital Selector
                    ExposedDropdownMenuBox(
                        expanded = isHospitalMenuOpen,
                        onExpandedChange = { isHospitalMenuOpen = !isHospitalMenuOpen }
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedHospital.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Target Hospital for OP Submission") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isHospitalMenuOpen) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("select_hospital_dropdown")
                        )
                        ExposedDropdownMenu(
                            expanded = isHospitalMenuOpen,
                            onDismissRequest = { isHospitalMenuOpen = false }
                        ) {
                            doctorSummaryViewModel.hospitalsList.forEach { hosp ->
                                DropdownMenuItem(
                                    text = { Text("${hosp.name} (${hosp.city})") },
                                    onClick = {
                                        doctorSummaryViewModel.selectHospital(hosp)
                                        isHospitalMenuOpen = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Department Selector
                    ExposedDropdownMenuBox(
                        expanded = isDeptMenuOpen,
                        onExpandedChange = { isDeptMenuOpen = !isDeptMenuOpen }
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedDepartment,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Target OP Specialty Department") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDeptMenuOpen) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("select_dept_dropdown")
                        )
                        ExposedDropdownMenu(
                            expanded = isDeptMenuOpen,
                            onDismissRequest = { isDeptMenuOpen = false }
                        ) {
                            uiState.selectedHospital.departments.forEach { dept ->
                                DropdownMenuItem(
                                    text = { Text(dept) },
                                    onClick = {
                                        doctorSummaryViewModel.selectDepartment(dept)
                                        isDeptMenuOpen = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { doctorSummaryViewModel.generateSummary() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("generate_doctor_summary_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (uiState.isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Compiling Clinical SOAP...")
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Generate Clinical Doctor Summary", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Active Generated Summary Card (Clinical SOAP format)
        if (uiState.currentSummary != null) {
            val sum = uiState.currentSummary!!
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Generated OP Doctor Brief (SOAP)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generated_summary_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Patient: ${sum.patientName} (${sum.patientAge}y/${sum.patientGender})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (sum.isSubmittedToHospital) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                                ) {
                                    Text(
                                        text = if (sum.isSubmittedToHospital) "Submitted to OP" else "Draft Ready",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sum.isSubmittedToHospital) Color(0xFF15803D) else Color(0xFFB45309),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = "1. Chief Complaint (CC):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MedicalTealDark)
                            Text(text = sum.chiefComplaint, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(text = "2. History of Present Illness (HPI):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MedicalTealDark)
                            Text(text = sum.historyOfPresentIllness, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(text = "3. Pertinent Negatives & Red Flags:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MedicalTealDark)
                            Text(text = sum.pertinentNegativesAndFlags, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(text = "4. Key Inquiries for OP Doctor:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MedicalTealDark)
                            Text(text = sum.recommendedQuestionsForDoctor, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(12.dp))

                            // Submit to Hospital OP System button
                            Button(
                                onClick = { doctorSummaryViewModel.submitToHospitalOpSystem() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("submit_to_hospital_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalBluePrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                if (uiState.isSubmitting) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Transmitting to Hospital OP System...")
                                } else {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (sum.isSubmittedToHospital) "Re-Submit to ${sum.targetHospital}" else "Submit Summary to ${sum.targetHospital} OP System",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Summary History
        if (summariesHistory.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = "Previous Summaries & OP Submissions (${summariesHistory.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(summariesHistory) { itemSummary ->
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { doctorSummaryViewModel.selectHistorySummary(itemSummary) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = itemSummary.targetHospital,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MedicalTealDark
                                )
                                Text(
                                    text = itemSummary.targetDepartment,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = itemSummary.chiefComplaint,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2
                            )
                            if (!itemSummary.hospitalAckToken.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Ack Token: ${itemSummary.hospitalAckToken}",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF15803D),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Success Submission Dialog
    if (uiState.isSuccessDialogOpen) {
        AlertDialog(
            onDismissRequest = { doctorSummaryViewModel.dismissSuccessDialog() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Transmitted to Hospital OP System!", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Your clinical doctor summary has been dispatched directly into ${uiState.selectedHospital.name}'s OP Doctor Dashboard.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEFF6FF),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "OP System ACK Token:", fontSize = 10.sp, color = MedicalBluePrimary, fontWeight = FontWeight.Bold)
                            Text(
                                text = uiState.submissionAckToken ?: "ACK-HOSP-2026-OP",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF1E3A8A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Hospital OP Doctor will receive this summary on their terminal when your token is called.",
                                fontSize = 11.sp,
                                color = Color(0xFF1E40AF)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { doctorSummaryViewModel.dismissSuccessDialog() },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary)
                ) {
                    Text("OK")
                }
            }
        )
    }
}
