package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HealthMetricRecord
import com.example.data.model.MetricType
import com.example.ui.components.ClinicalTriageDonutChart
import com.example.ui.components.HealthMetricCard
import com.example.ui.components.InteractiveHealthBarChart
import com.example.ui.components.InteractiveHealthLineChart
import com.example.ui.viewmodel.HealthAnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthAnalyticsScreen(
    analyticsViewModel: HealthAnalyticsViewModel,
    onNavigateToDoctorSummary: () -> Unit = {},
    onNavigateToReports: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by analyticsViewModel.uiState.collectAsState()
    val currentUser by analyticsViewModel.currentUser.collectAsState()
    val allMetrics by analyticsViewModel.allMetrics.collectAsState()
    val doctorSummaries by analyticsViewModel.doctorSummaries.collectAsState()
    val medicalReports by analyticsViewModel.medicalReports.collectAsState()
    val opRegistrations by analyticsViewModel.opRegistrations.collectAsState()
    val clinicalAnalytics by analyticsViewModel.clinicalAnalytics.collectAsState()

    var showShareSheet by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            analyticsViewModel.clearToast()
        }
    }

    // Filter metrics by timeframe
    val now = System.currentTimeMillis()
    val dayMs = 86400000L
    val filteredMetrics = remember(allMetrics, uiState.selectedTimeframe) {
        val days = when (uiState.selectedTimeframe) {
            "7D" -> 7
            "30D" -> 30
            "3M" -> 90
            "6M" -> 180
            "1Y" -> 365
            else -> 1000
        }
        val cutoff = now - (days * dayMs)
        allMetrics.filter { it.timestamp >= cutoff }
    }

    val bpMetrics = filteredMetrics.filter { it.metricType == MetricType.BLOOD_PRESSURE.name }
    val glucoseMetrics = filteredMetrics.filter { it.metricType == MetricType.BLOOD_GLUCOSE.name }
    val hrMetrics = filteredMetrics.filter { it.metricType == MetricType.HEART_RATE.name }
    val hba1cMetrics = filteredMetrics.filter { it.metricType == MetricType.HBA1C.name }
    val cholMetrics = filteredMetrics.filter { it.metricType == MetricType.CHOLESTEROL.name }
    val wtMetrics = filteredMetrics.filter { it.metricType == MetricType.BODY_WEIGHT.name }
    val spo2Metrics = filteredMetrics.filter { it.metricType == MetricType.OXYGEN_SPO2.name }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Health Analytics & Trends",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${currentUser?.fullName ?: "Patient"} • ABHA Health Data Visualizer",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showShareSheet = true },
                        modifier = Modifier.testTag("btn_share_trends")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share Clinical Trends",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { analyticsViewModel.openAddReadingDialog(true) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_health_reading")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Log Reading")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Log Reading", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // 1. Timeframe Chips Bar
            item {
                TimeframeSelectorRow(
                    selected = uiState.selectedTimeframe,
                    onSelected = { analyticsViewModel.setTimeframe(it) }
                )
            }

            // 2. Metric Category Tabs
            item {
                val categories = listOf("Overview", "Blood Pressure", "Blood Glucose", "Vitals & Heart", "Clinical Summaries")
                ScrollableTabRow(
                    selectedTabIndex = categories.indexOf(uiState.selectedCategory).coerceAtLeast(0),
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabPositions ->
                        val index = categories.indexOf(uiState.selectedCategory).coerceAtLeast(0)
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[index]),
                            color = MaterialTheme.colorScheme.primary,
                            height = 3.dp
                        )
                    }
                ) {
                    categories.forEach { cat ->
                        val isSelected = uiState.selectedCategory == cat
                        Tab(
                            selected = isSelected,
                            onClick = { analyticsViewModel.setCategory(cat) },
                            text = {
                                Text(
                                    text = cat,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 3. Category Specific Views
            when (uiState.selectedCategory) {
                "Overview" -> {
                    item {
                        OverviewSummarySection(
                            clinicalAnalytics = clinicalAnalytics,
                            bpMetrics = bpMetrics,
                            glucoseMetrics = glucoseMetrics,
                            hba1cMetrics = hba1cMetrics,
                            wtMetrics = wtMetrics,
                            onSelectBp = { analyticsViewModel.setCategory("Blood Pressure") },
                            onSelectGlucose = { analyticsViewModel.setCategory("Blood Glucose") },
                            onSelectSummaries = { analyticsViewModel.setCategory("Clinical Summaries") }
                        )
                    }
                }
                "Blood Pressure" -> {
                    item {
                        BloodPressureDetailSection(
                            bpMetrics = bpMetrics,
                            onPointSelected = { analyticsViewModel.selectDataPoint(it) }
                        )
                    }
                }
                "Blood Glucose" -> {
                    item {
                        BloodGlucoseDetailSection(
                            glucoseMetrics = glucoseMetrics,
                            hba1cMetrics = hba1cMetrics,
                            onPointSelected = { analyticsViewModel.selectDataPoint(it) }
                        )
                    }
                }
                "Vitals & Heart" -> {
                    item {
                        VitalsDetailSection(
                            hrMetrics = hrMetrics,
                            spo2Metrics = spo2Metrics,
                            wtMetrics = wtMetrics,
                            cholMetrics = cholMetrics,
                            onPointSelected = { analyticsViewModel.selectDataPoint(it) }
                        )
                    }
                }
                "Clinical Summaries" -> {
                    item {
                        ClinicalSummariesDetailSection(
                            clinicalAnalytics = clinicalAnalytics,
                            doctorSummaries = doctorSummaries,
                            opRegistrations = opRegistrations,
                            medicalReports = medicalReports,
                            onNavigateToDoctorSummary = onNavigateToDoctorSummary,
                            onNavigateToReports = onNavigateToReports
                        )
                    }
                }
            }
        }
    }

    // Add Reading Dialog Modal
    if (uiState.isAddReadingDialogOpen) {
        AddHealthReadingDialog(
            uiState = uiState,
            onDismiss = { analyticsViewModel.openAddReadingDialog(false) },
            onTypeChange = { analyticsViewModel.updateNewMetricType(it) },
            onPrimaryChange = { analyticsViewModel.updateNewPrimaryValue(it) },
            onSecondaryChange = { analyticsViewModel.updateNewSecondaryValue(it) },
            onContextChange = { analyticsViewModel.updateNewContextTag(it) },
            onNotesChange = { analyticsViewModel.updateNewNotes(it) },
            onSave = { analyticsViewModel.saveNewHealthReading() }
        )
    }

    // Share / Clinical Export Sheet
    if (showShareSheet) {
        ClinicalExportModal(
            patientName = currentUser?.fullName ?: "Durga Reddy",
            aadhaar = currentUser?.aadhaarNumber ?: "9876-5432-1098",
            clinicalAnalytics = clinicalAnalytics,
            onDismiss = { showShareSheet = false }
        )
    }
}

@Composable
private fun TimeframeSelectorRow(
    selected: String,
    onSelected: (String) -> Unit
) {
    val options = listOf("7D", "30D", "3M", "6M", "1Y", "All")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Timeframe:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        options.forEach { opt ->
            val isSelected = selected == opt
            FilterChip(
                selected = isSelected,
                onClick = { onSelected(opt) },
                label = {
                    Text(
                        text = opt,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(32.dp)
            )
        }
    }
}

@Composable
private fun OverviewSummarySection(
    clinicalAnalytics: com.example.ui.viewmodel.ClinicalSummaryAnalytics,
    bpMetrics: List<HealthMetricRecord>,
    glucoseMetrics: List<HealthMetricRecord>,
    hba1cMetrics: List<HealthMetricRecord>,
    wtMetrics: List<HealthMetricRecord>,
    onSelectBp: () -> Unit,
    onSelectGlucose: () -> Unit,
    onSelectSummaries: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall Vitals Health Status Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF00695C),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.HealthAndSafety,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Clinical Health Score: 92/100",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Vitals, BP, and Fasting Glucose are stable and within optimal target ranges.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // Section Title: Primary Vitals Trends
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Blood Pressure Progression",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tap chart for details",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Interactive BP Line Chart
        InteractiveHealthLineChart(
            metrics = bpMetrics,
            metricType = MetricType.BLOOD_PRESSURE
        )

        // Section Title: Glucose & HbA1c
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Blood Glucose Trajectory",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Target: <100 mg/dL",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.SemiBold
            )
        }

        InteractiveHealthLineChart(
            metrics = glucoseMetrics,
            metricType = MetricType.BLOOD_GLUCOSE
        )

        // Clinical Summary Breakdown Donut
        Text(
            text = "AI Triage & Clinical Summary Distribution",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ClinicalTriageDonutChart(
            urgencyCounts = clinicalAnalytics.urgencyCounts,
            totalSummaries = clinicalAnalytics.totalSummaries
        )

        // Departmental Visit Bar Chart
        if (clinicalAnalytics.departmentCounts.isNotEmpty()) {
            Text(
                text = "Clinical Consultations by Department",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val deptList = clinicalAnalytics.departmentCounts.map { Pair(it.key, it.value) }
            InteractiveHealthBarChart(
                categories = deptList,
                unitLabel = "Visits",
                barColor = Color(0xFF1976D2)
            )
        }

        // 4 Grid Quick Metric Cards
        Text(
            text = "Latest Key Metrics Summary",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        clinicalAnalytics.latestBp?.let { bp ->
            HealthMetricCard(
                title = "Blood Pressure (Resting)",
                latestValue = "${bp.valuePrimary.toInt()}/${bp.valueSecondary?.toInt() ?: 80}",
                unit = bp.unit,
                statusText = bp.getClinicalStatus(),
                statusColor = Color(0xFF2E7D32),
                contextTag = bp.contextTag,
                dateString = bp.dateString,
                trendPercent = "-14.5% vs 30d",
                isTrendingDown = true,
                isGoodTrend = true,
                onClick = onSelectBp
            )
        }

        clinicalAnalytics.latestGlucose?.let { glu ->
            HealthMetricCard(
                title = "Fasting Blood Sugar",
                latestValue = "${glu.valuePrimary.toInt()}",
                unit = glu.unit,
                statusText = glu.getClinicalStatus(),
                statusColor = Color(0xFF2E7D32),
                contextTag = glu.contextTag,
                dateString = glu.dateString,
                trendPercent = "-23.7% vs 30d",
                isTrendingDown = true,
                isGoodTrend = true,
                onClick = onSelectGlucose
            )
        }

        clinicalAnalytics.latestHbA1c?.let { hba1c ->
            HealthMetricCard(
                title = "Glycated Hemoglobin (HbA1c)",
                latestValue = "${hba1c.valuePrimary}%",
                unit = "HbA1c",
                statusText = hba1c.getClinicalStatus(),
                statusColor = Color(0xFF2E7D32),
                contextTag = hba1c.contextTag,
                dateString = hba1c.dateString,
                trendPercent = "-1.3% vs 9m",
                isTrendingDown = true,
                isGoodTrend = true,
                onClick = onSelectGlucose
            )
        }
    }
}

@Composable
private fun BloodPressureDetailSection(
    bpMetrics: List<HealthMetricRecord>,
    onPointSelected: (HealthMetricRecord?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Clinical guidance header card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.MonitorHeart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Blood Pressure Target: <120 / <80 mmHg",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "AHA & ICMR clinical guidelines recommend keeping resting systolic under 120 mmHg and diastolic under 80 mmHg.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                )
            }
        }

        // Main BP Interactive Line Chart
        InteractiveHealthLineChart(
            metrics = bpMetrics,
            metricType = MetricType.BLOOD_PRESSURE,
            onPointSelected = onPointSelected
        )

        // Historical Log Table
        Text(
            text = "Recorded Readings Log (${bpMetrics.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        bpMetrics.reversed().forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = item.formattedReading(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${item.dateString} • ${item.contextTag}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (item.notes.isNotEmpty()) {
                            Text(
                                text = item.notes,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when {
                            item.getClinicalStatus().contains("Optimal", ignoreCase = true) -> Color(0xFFE8F5E9)
                            item.getClinicalStatus().contains("Elevated", ignoreCase = true) -> Color(0xFFFFF3E0)
                            else -> Color(0xFFFFEBEE)
                        }
                    ) {
                        Text(
                            text = item.getClinicalStatus(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                item.getClinicalStatus().contains("Optimal", ignoreCase = true) -> Color(0xFF2E7D32)
                                item.getClinicalStatus().contains("Elevated", ignoreCase = true) -> Color(0xFFE65100)
                                else -> Color(0xFFC62828)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BloodGlucoseDetailSection(
    glucoseMetrics: List<HealthMetricRecord>,
    hba1cMetrics: List<HealthMetricRecord>,
    onPointSelected: (HealthMetricRecord?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Glucose Chart
        Text(
            text = "Blood Glucose Readings (Fasting & Post-Meal)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        InteractiveHealthLineChart(
            metrics = glucoseMetrics,
            metricType = MetricType.BLOOD_GLUCOSE,
            onPointSelected = onPointSelected
        )

        // HbA1c Progression Chart
        if (hba1cMetrics.isNotEmpty()) {
            Text(
                text = "HbA1c Glycated Hemoglobin Trend (%)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            InteractiveHealthLineChart(
                metrics = hba1cMetrics,
                metricType = MetricType.HBA1C,
                onPointSelected = onPointSelected
            )
        }
    }
}

@Composable
private fun VitalsDetailSection(
    hrMetrics: List<HealthMetricRecord>,
    spo2Metrics: List<HealthMetricRecord>,
    wtMetrics: List<HealthMetricRecord>,
    cholMetrics: List<HealthMetricRecord>,
    onPointSelected: (HealthMetricRecord?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Resting Heart Rate
        Text(
            text = "Resting Heart Rate (bpm)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        InteractiveHealthLineChart(
            metrics = hrMetrics,
            metricType = MetricType.HEART_RATE,
            onPointSelected = onPointSelected
        )

        // Body Weight Progression
        if (wtMetrics.isNotEmpty()) {
            Text(
                text = "Body Weight Progression (kg)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            InteractiveHealthLineChart(
                metrics = wtMetrics,
                metricType = MetricType.BODY_WEIGHT,
                onPointSelected = onPointSelected
            )
        }

        // Cholesterol
        if (cholMetrics.isNotEmpty()) {
            Text(
                text = "Total Cholesterol (mg/dL)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            InteractiveHealthLineChart(
                metrics = cholMetrics,
                metricType = MetricType.CHOLESTEROL,
                onPointSelected = onPointSelected
            )
        }
    }
}

@Composable
private fun ClinicalSummariesDetailSection(
    clinicalAnalytics: com.example.ui.viewmodel.ClinicalSummaryAnalytics,
    doctorSummaries: List<com.example.data.model.DoctorSummaryRecord>,
    opRegistrations: List<com.example.data.model.OpRegistration>,
    medicalReports: List<com.example.data.model.MedicalReport>,
    onNavigateToDoctorSummary: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Clinical Triage Donut
        Text(
            text = "Clinical Triage Severity Breakdown",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ClinicalTriageDonutChart(
            urgencyCounts = clinicalAnalytics.urgencyCounts,
            totalSummaries = clinicalAnalytics.totalSummaries
        )

        // Departmental Consultations Bar Chart
        if (clinicalAnalytics.departmentCounts.isNotEmpty()) {
            Text(
                text = "Consultations & OP Visits by Specialty",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val deptList = clinicalAnalytics.departmentCounts.map { Pair(it.key, it.value) }
            InteractiveHealthBarChart(
                categories = deptList,
                unitLabel = "Visits",
                barColor = Color(0xFF00897B)
            )
        }

        // Clinical Reports & Doctor Summaries list
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Generated Doctor Summaries (${doctorSummaries.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToDoctorSummary() }
            )
        }

        doctorSummaries.forEach { summary ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = summary.targetDepartment,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (summary.suggestedTriageCategory == "Emergency") Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = summary.suggestedTriageCategory,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (summary.suggestedTriageCategory == "Emergency") Color(0xFFC62828) else Color(0xFF2E7D32)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Chief Complaint: ${summary.chiefComplaint}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Hospital: ${summary.targetHospital}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun AddHealthReadingDialog(
    uiState: com.example.ui.viewmodel.HealthAnalyticsUiState,
    onDismiss: () -> Unit,
    onTypeChange: (MetricType) -> Unit,
    onPrimaryChange: (String) -> Unit,
    onSecondaryChange: (String) -> Unit,
    onContextChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log Health Measurement",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Vital Metric:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // Metric Selector Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricType.values().forEach { type ->
                        val isSelected = uiState.newMetricType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { onTypeChange(type) },
                            label = {
                                Text(
                                    text = type.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                // Primary Value Input
                OutlinedTextField(
                    value = uiState.newPrimaryValue,
                    onValueChange = onPrimaryChange,
                    label = {
                        Text(
                            text = if (uiState.newMetricType == MetricType.BLOOD_PRESSURE) "Systolic (${uiState.newMetricType.unit})"
                            else "Value (${uiState.newMetricType.unit})"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Secondary Value Input (for Blood Pressure)
                if (uiState.newMetricType == MetricType.BLOOD_PRESSURE) {
                    OutlinedTextField(
                        value = uiState.newSecondaryValue,
                        onValueChange = onSecondaryChange,
                        label = { Text("Diastolic (${uiState.newMetricType.unit})") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Context Tag (e.g. Fasting, Resting)
                OutlinedTextField(
                    value = uiState.newContextTag,
                    onValueChange = onContextChange,
                    label = { Text("Context Tag (e.g. Fasting, Post-Meal, Resting)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Clinical Notes
                OutlinedTextField(
                    value = uiState.newNotes,
                    onValueChange = onNotesChange,
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = !uiState.isSaving && uiState.newPrimaryValue.isNotBlank(),
                modifier = Modifier.testTag("btn_confirm_save_reading")
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save Reading")
                }
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ClinicalExportModal(
    patientName: String,
    aadhaar: String,
    clinicalAnalytics: com.example.ui.viewmodel.ClinicalSummaryAnalytics,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Clinical Trend Report", fontWeight = FontWeight.Bold)
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Patient: $patientName (Aadhaar: $aadhaar)",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Generated: August 2026 • AarogyaCare ABHA Vault",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Text(
                    text = "• Blood Pressure Trend: Avg ${clinicalAnalytics.avgBpSystolic}/${clinicalAnalytics.avgBpDiastolic} mmHg (Controlled)",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Latest Fasting Glucose: ${clinicalAnalytics.latestGlucose?.valuePrimary?.toInt() ?: 90} mg/dL (Normal Range)",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Latest HbA1c: ${clinicalAnalytics.latestHbA1c?.valuePrimary ?: 5.5}% (Controlled)",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Clinical Summaries: ${clinicalAnalytics.totalSummaries} Doctor Reports filed across ${clinicalAnalytics.departmentCounts.size} departments",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Daily Medication Adherence: ${clinicalAnalytics.medicationAdherenceRate}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Toast.makeText(context, "Clinical Trend Summary copied & ready to share with your Doctor!", Toast.LENGTH_LONG).show()
                    onDismiss()
                }
            ) {
                Text("Share with Doctor")
            }
        }
    )
}
