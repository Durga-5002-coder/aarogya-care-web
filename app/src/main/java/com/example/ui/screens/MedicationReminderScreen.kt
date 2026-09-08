package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.MedicationReminder
import com.example.ui.theme.MedicalAccentAmber
import com.example.ui.theme.MedicalBlueDark
import com.example.ui.theme.MedicalBluePrimary
import com.example.ui.theme.MedicalTealDark
import com.example.ui.theme.MedicalTealLight
import com.example.ui.theme.MedicalTealPrimary
import com.example.ui.viewmodel.MedicationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MedicationReminderScreen(
    medicationViewModel: MedicationViewModel
) {
    val context = LocalContext.current
    val reminders by medicationViewModel.reminders.collectAsState()
    val uiState by medicationViewModel.uiState.collectAsState()

    val todayStr = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            Toast.makeText(context, "Notification alerts enabled for medications!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.showTestToastMessage) {
        uiState.showTestToastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            medicationViewModel.clearToastMessage()
        }
    }

    val filteredReminders = remember(reminders, uiState.selectedFilter) {
        when (uiState.selectedFilter) {
            "Morning" -> reminders.filter { it.timeHour in 5..11 }
            "Afternoon" -> reminders.filter { it.timeHour in 12..16 }
            "Evening" -> reminders.filter { it.timeHour in 17..20 }
            "Night" -> reminders.filter { it.timeHour !in 5..20 }
            else -> reminders
        }
    }

    val totalCount = reminders.size
    val activeCount = reminders.count { it.isActive }
    val takenTodayCount = reminders.count { it.lastTakenDate == todayStr }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { medicationViewModel.openAddDialog(true) },
                containerColor = MedicalTealPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 60.dp)
                    .testTag("add_medication_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Medication Reminder")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .testTag("medication_reminder_screen"),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Screen Header & Adherence Progress Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Medication Schedule & Alarms",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Daily prescription reminders stored in Room & local notifications",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MedicalTealLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Medication,
                                    contentDescription = null,
                                    tint = MedicalTealDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Total Active
                            StatCard(
                                title = "Active Reminders",
                                value = "$activeCount / $totalCount",
                                subtitle = "Active daily alarms",
                                color = MedicalTealPrimary,
                                modifier = Modifier.weight(1f)
                            )

                            // Taken Today
                            StatCard(
                                title = "Doses Taken Today",
                                value = "$takenTodayCount / $totalCount",
                                subtitle = if (totalCount > 0 && takenTodayCount == totalCount) "100% Adherence 🎉" else "Keep it up!",
                                color = if (takenTodayCount == totalCount && totalCount > 0) Color(0xFF16A34A) else MedicalBluePrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Notification Permission Warning if not granted
            if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("permission_banner"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Allow Notifications for Alarms",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF92400E)
                                )
                                Text(
                                    text = "To receive sound and lock-screen alerts when it's time to take your pills.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFB45309)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("Enable", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Live Test Notification Trigger Box
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("live_test_notification_card"),
                    shape = RoundedCornerShape(14.dp),
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
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(MedicalTealLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = MedicalTealDark,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Test Local Notification System",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Fires a live reminder notification with 'Take' & 'Snooze' actions",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (reminders.isNotEmpty()) {
                                    medicationViewModel.triggerTestNotification(reminders.first())
                                } else {
                                    val demo = MedicationReminder(
                                        userAadhaar = "5482 9104 3821",
                                        medicineName = "Pantoprazole 40mg",
                                        dosage = "1 Capsule",
                                        instruction = "Before Breakfast",
                                        timeHour = 8,
                                        timeMinute = 0,
                                        category = "Gastro Care"
                                    )
                                    medicationViewModel.triggerTestNotification(demo)
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary),
                            modifier = Modifier.testTag("trigger_test_notification_btn"),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Alarm, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Test Alarm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Filter Chips (All, Morning, Afternoon, Evening, Night)
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf("All", "Morning", "Afternoon", "Evening", "Night")
                    items(filters) { filter ->
                        val isSelected = uiState.selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { medicationViewModel.setFilter(filter) },
                            label = { Text(filter, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MedicalTealPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("filter_chip_$filter")
                        )
                    }
                }
            }

            // Reminders List Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Scheduled Doses (${filteredReminders.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Auto-repeats daily",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Empty State
            if (filteredReminders.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Medication,
                                contentDescription = null,
                                tint = MedicalTealDark.copy(alpha = 0.5f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No medication scheduled for ${uiState.selectedFilter}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap the '+' button below to add your prescribed medicines and set daily alarm times.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredReminders, key = { it.id }) { reminder ->
                    MedicationReminderItemCard(
                        reminder = reminder,
                        isTakenToday = reminder.lastTakenDate == todayStr,
                        onToggleActive = { isActive ->
                            medicationViewModel.toggleMedicationActive(reminder, isActive)
                        },
                        onMarkTaken = {
                            medicationViewModel.markTaken(reminder)
                        },
                        onTestAlert = {
                            medicationViewModel.triggerTestNotification(reminder)
                        },
                        onDelete = {
                            medicationViewModel.deleteMedication(reminder)
                        }
                    )
                }
            }
        }
    }

    // Add Medication Dialog
    if (uiState.isAddDialogOpen) {
        AddMedicationDialog(
            medicineName = uiState.medicineName,
            dosage = uiState.dosage,
            instruction = uiState.instruction,
            timeHour = uiState.timeHour,
            timeMinute = uiState.timeMinute,
            category = uiState.category,
            onNameChange = { medicationViewModel.updateMedicineName(it) },
            onDosageChange = { medicationViewModel.updateDosage(it) },
            onInstructionChange = { medicationViewModel.updateInstruction(it) },
            onTimeChange = { h, m -> medicationViewModel.updateTime(h, m) },
            onCategoryChange = { medicationViewModel.updateCategory(it) },
            onSave = { medicationViewModel.saveMedicationReminder() },
            onDismiss = { medicationViewModel.openAddDialog(false) }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun MedicationReminderItemCard(
    reminder: MedicationReminder,
    isTakenToday: Boolean,
    onToggleActive: (Boolean) -> Unit,
    onMarkTaken: () -> Unit,
    onTestAlert: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("medication_card_${reminder.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Time, Category Pill, Active Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (reminder.isActive) MedicalTealLight else Color(0xFFF3F4F6)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = if (reminder.isActive) MedicalTealDark else Color.Gray,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = reminder.formattedTime(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (reminder.isActive) MedicalTealDark else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF0F9FF)
                    ) {
                        Text(
                            text = reminder.category,
                            fontSize = 10.sp,
                            color = MedicalBlueDark,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Switch(
                    checked = reminder.isActive,
                    onCheckedChange = onToggleActive,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MedicalTealPrimary
                    ),
                    modifier = Modifier.testTag("toggle_reminder_${reminder.id}")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Medicine Name & Dosage
            Text(
                text = reminder.medicineName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Dosage: ${reminder.dosage}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = " • ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = reminder.instruction,
                    fontSize = 12.sp,
                    color = MedicalTealDark,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (reminder.streakDays > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🔥 ${reminder.streakDays}-day streak",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEA580C)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Actions: Take Dose Checkbox/Button + Test Alert Bell + Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mark Taken / Taken Status
                if (isTakenToday) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Taken Today",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onMarkTaken,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary),
                        modifier = Modifier.testTag("mark_taken_${reminder.id}"),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mark Taken", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Test live alert button
                    IconButton(
                        onClick = onTestAlert,
                        modifier = Modifier.testTag("test_alert_${reminder.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Test Notification Alarm",
                            tint = MedicalTealDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.testTag("delete_reminder_${reminder.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete Reminder",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddMedicationDialog(
    medicineName: String,
    dosage: String,
    instruction: String,
    timeHour: Int,
    timeMinute: Int,
    category: String,
    onNameChange: (String) -> Unit,
    onDosageChange: (String) -> Unit,
    onInstructionChange: (String) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val quickMedicines = listOf(
        "Pantoprazole 40mg", "Metformin 500mg", "Telmisartan 40mg",
        "Paracetamol 650mg", "Amoxicillin 250mg", "Rosuvastatin 10mg",
        "Vitamin D3 60k", "Cetirizine 10mg"
    )

    val quickDosages = listOf("1 Tablet", "0.5 Tablet", "2 Tablets", "1 Capsule", "5 ml Syrup", "2 Puffs")

    val quickInstructions = listOf(
        "Before Breakfast", "After Breakfast",
        "Before Lunch", "After Lunch",
        "Before Dinner", "After Dinner",
        "At Bedtime", "Empty Stomach"
    )

    val quickTimes = listOf(
        Pair("07:30 AM (Morning)", Pair(7, 30)),
        Pair("08:30 AM (Breakfast)", Pair(8, 30)),
        Pair("01:30 PM (Lunch)", Pair(13, 30)),
        Pair("08:00 PM (Dinner)", Pair(20, 0)),
        Pair("09:30 PM (Bedtime)", Pair(21, 30))
    )

    val categories = listOf("General", "Diabetes", "Blood Pressure", "Gastro Care", "Antibiotic", "Pain Relief", "Vitamin/Supplement")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AlarmOn, contentDescription = null, tint = MedicalTealPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Medication Schedule", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Medicine Name
                item {
                    Text(text = "Medicine Name:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = medicineName,
                        onValueChange = onNameChange,
                        placeholder = { Text("e.g. Metformin 500mg") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dialog_med_name_input")
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    // Quick chips for medicine names
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(quickMedicines) { med ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (medicineName == med) MedicalTealPrimary else Color(0xFFF1F5F9),
                                modifier = Modifier.clickable { onNameChange(med) }
                            ) {
                                Text(
                                    text = med,
                                    fontSize = 10.sp,
                                    color = if (medicineName == med) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Dosage
                item {
                    Text(text = "Dosage / Quantity:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = onDosageChange,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dialog_dosage_input")
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(quickDosages) { dos ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (dosage == dos) MedicalTealPrimary else Color(0xFFF1F5F9),
                                modifier = Modifier.clickable { onDosageChange(dos) }
                            ) {
                                Text(
                                    text = dos,
                                    fontSize = 10.sp,
                                    color = if (dosage == dos) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Timing & Food Instructions
                item {
                    Text(text = "Meal Instruction:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = instruction,
                        onValueChange = onInstructionChange,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dialog_instruction_input")
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(quickInstructions) { inst ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (instruction == inst) MedicalTealPrimary else Color(0xFFF1F5F9),
                                modifier = Modifier.clickable { onInstructionChange(inst) }
                            ) {
                                Text(
                                    text = inst,
                                    fontSize = 10.sp,
                                    color = if (instruction == inst) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Schedule Daily Time
                item {
                    val hour12 = if (timeHour == 0) 12 else if (timeHour > 12) timeHour - 12 else timeHour
                    val amPm = if (timeHour >= 12) "PM" else "AM"
                    val minStr = if (timeMinute < 10) "0$timeMinute" else "$timeMinute"

                    Text(text = "Daily Alarm Time: $hour12:$minStr $amPm", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MedicalTealDark)

                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        quickTimes.forEach { (label, timePair) ->
                            val isSelected = timeHour == timePair.first && timeMinute == timePair.second
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MedicalTealLight else Color(0xFFF8FAFC),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) MedicalTealPrimary else Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onTimeChange(timePair.first, timePair.second) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = if (isSelected) MedicalTealDark else Color.Gray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MedicalTealDark else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                // Medical Category
                item {
                    Text(text = "Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(categories) { cat ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (category == cat) MedicalBluePrimary else Color(0xFFF1F5F9),
                                modifier = Modifier.clickable { onCategoryChange(cat) }
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 10.sp,
                                    color = if (category == cat) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = medicineName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MedicalTealPrimary),
                modifier = Modifier.testTag("save_medication_reminder_btn")
            ) {
                Text("Set Alarm & Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
