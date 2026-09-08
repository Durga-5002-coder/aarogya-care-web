package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HealthMetricRecord
import com.example.data.model.MetricType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Interactive Multi-Series Health Metric Line & Area Chart
 */
@Composable
fun InteractiveHealthLineChart(
    metrics: List<HealthMetricRecord>,
    metricType: MetricType,
    modifier: Modifier = Modifier,
    onPointSelected: (HealthMetricRecord?) -> Unit = {}
) {
    if (metrics.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No recorded readings for selected timeframe",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val isDualSeries = metricType == MetricType.BLOOD_PRESSURE
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(metrics) {
        selectedIndex = null
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, animationSpec = tween(700))
    }

    val primaryColor = when (metricType) {
        MetricType.BLOOD_PRESSURE -> Color(0xFF00897B) // Teal
        MetricType.BLOOD_GLUCOSE -> Color(0xFFE65100) // Deep Orange
        MetricType.HEART_RATE -> Color(0xFFD32F2F) // Red
        MetricType.OXYGEN_SPO2 -> Color(0xFF0288D1) // Light Blue
        MetricType.HBA1C -> Color(0xFF6A1B9A) // Purple
        MetricType.CHOLESTEROL -> Color(0xFFF57C00) // Orange
        MetricType.BODY_WEIGHT -> Color(0xFF2E7D32) // Forest Green
    }

    val secondaryColor = Color(0xFF1E88E5) // Blue for Diastolic BP

    // Compute min and max values with padding
    val allPrimary = metrics.map { it.valuePrimary }
    val allSecondary = if (isDualSeries) metrics.mapNotNull { it.valueSecondary } else emptyList()
    val allValues = allPrimary + allSecondary + listOf(metricType.normalMin, metricType.normalMax)

    val rawMin = allValues.minOrNull() ?: 0.0
    val rawMax = allValues.maxOrNull() ?: 100.0
    val yMin = (rawMin * 0.85).toFloat()
    val yMax = (rawMax * 1.15).toFloat()
    val yRange = max(1f, yMax - yMin)

    Column(modifier = modifier.fillMaxWidth()) {
        // Chart Header Legend & Selected Value Callout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(primaryColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isDualSeries) "Systolic" else metricType.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (isDualSeries) {
                    Spacer(modifier = Modifier.width(14.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(secondaryColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Diastolic",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Safe Zone Legend
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFF4CAF50).copy(alpha = 0.35f), RoundedCornerShape(2.dp))
                        .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.8f), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Normal Target Zone",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Active Hover / Tap Tooltip
        val activeItem = selectedIndex?.let { metrics.getOrNull(it) } ?: metrics.lastOrNull()
        if (activeItem != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(12.dp),
                color = primaryColor.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${activeItem.dateString} • ${activeItem.contextTag}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = activeItem.formattedReading(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when {
                            activeItem.getClinicalStatus().contains("Optimal", ignoreCase = true) ||
                                activeItem.getClinicalStatus().contains("Normal", ignoreCase = true) -> Color(0xFFE8F5E9)
                            activeItem.getClinicalStatus().contains("Elevated", ignoreCase = true) ||
                                activeItem.getClinicalStatus().contains("Pre", ignoreCase = true) -> Color(0xFFFFF3E0)
                            else -> Color(0xFFFFEBEE)
                        }
                    ) {
                        Text(
                            text = activeItem.getClinicalStatus(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                activeItem.getClinicalStatus().contains("Optimal", ignoreCase = true) ||
                                    activeItem.getClinicalStatus().contains("Normal", ignoreCase = true) -> Color(0xFF2E7D32)
                                activeItem.getClinicalStatus().contains("Elevated", ignoreCase = true) ||
                                    activeItem.getClinicalStatus().contains("Pre", ignoreCase = true) -> Color(0xFFE65100)
                                else -> Color(0xFFC62828)
                            }
                        )
                    }
                }
            }
        }

        // Native Canvas Line Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(top = 16.dp, bottom = 28.dp, start = 16.dp, end = 16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(metrics) {
                        detectTapGestures { offset ->
                            val n = metrics.size
                            if (n <= 1) {
                                selectedIndex = 0
                                onPointSelected(metrics.firstOrNull())
                                return@detectTapGestures
                            }
                            val spacing = size.width / (n - 1)
                            val idx = (offset.x / spacing + 0.5f).toInt().coerceIn(0, n - 1)
                            selectedIndex = idx
                            onPointSelected(metrics[idx])
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val n = metrics.size

                val stepX = if (n > 1) width / (n - 1) else width / 2

                fun getY(value: Double): Float {
                    val normalized = (value - yMin) / yRange
                    val y = height - (normalized * height).toFloat()
                    return y.coerceIn(0f, height)
                }

                // 1. Draw Normal Safe Range Zone Background
                val safeTop = getY(metricType.normalMax)
                val safeBottom = getY(metricType.normalMin)
                drawRect(
                    color = Color(0xFF4CAF50).copy(alpha = 0.08f),
                    topLeft = Offset(0f, min(safeTop, safeBottom)),
                    size = Size(width, abs(safeBottom - safeTop))
                )

                // Safe threshold dashed line
                drawLine(
                    color = Color(0xFF4CAF50).copy(alpha = 0.5f),
                    start = Offset(0f, safeTop),
                    end = Offset(width, safeTop),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                // 2. Draw Horizontal Gridlines & Y-Axis Reference Values
                val gridLines = 4
                for (i in 0..gridLines) {
                    val gridY = height * (i.toFloat() / gridLines)
                    val valueAtGrid = yMax - (i.toFloat() / gridLines) * yRange
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        start = Offset(0f, gridY),
                        end = Offset(width, gridY),
                        strokeWidth = 1f
                    )
                }

                // 3. Draw Diastolic Series (if dual)
                if (isDualSeries) {
                    val secondaryPoints = metrics.mapIndexedNotNull { i, m ->
                        m.valueSecondary?.let { sVal ->
                            val x = if (n > 1) i * stepX else width / 2
                            val y = getY(sVal)
                            Offset(x, y)
                        }
                    }

                    if (secondaryPoints.isNotEmpty()) {
                        val secondaryPath = Path()
                        secondaryPoints.forEachIndexed { i, pt ->
                            val animatedY = height - (height - pt.y) * animProgress.value
                            val animatedPt = Offset(pt.x, animatedY)
                            if (i == 0) secondaryPath.moveTo(animatedPt.x, animatedPt.y)
                            else secondaryPath.lineTo(animatedPt.x, animatedPt.y)
                        }

                        drawPath(
                            path = secondaryPath,
                            color = secondaryColor,
                            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                        )

                        secondaryPoints.forEachIndexed { i, pt ->
                            val animatedY = height - (height - pt.y) * animProgress.value
                            drawCircle(
                                color = Color.White,
                                radius = 4.dp.toPx(),
                                center = Offset(pt.x, animatedY)
                            )
                            drawCircle(
                                color = secondaryColor,
                                radius = 3.dp.toPx(),
                                center = Offset(pt.x, animatedY)
                            )
                        }
                    }
                }

                // 4. Draw Primary Series Line & Gradient Fill
                val primaryPoints = metrics.mapIndexed { i, m ->
                    val x = if (n > 1) i * stepX else width / 2
                    val y = getY(m.valuePrimary)
                    Offset(x, y)
                }

                if (primaryPoints.isNotEmpty()) {
                    val primaryPath = Path()
                    val fillPath = Path()

                    primaryPoints.forEachIndexed { i, pt ->
                        val animatedY = height - (height - pt.y) * animProgress.value
                        val animatedPt = Offset(pt.x, animatedY)
                        if (i == 0) {
                            primaryPath.moveTo(animatedPt.x, animatedPt.y)
                            fillPath.moveTo(animatedPt.x, height)
                            fillPath.lineTo(animatedPt.x, animatedPt.y)
                        } else {
                            primaryPath.lineTo(animatedPt.x, animatedPt.y)
                            fillPath.lineTo(animatedPt.x, animatedPt.y)
                        }
                    }

                    if (primaryPoints.isNotEmpty()) {
                        fillPath.lineTo(primaryPoints.last().x, height)
                        fillPath.close()

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.25f * animProgress.value),
                                    primaryColor.copy(alpha = 0.01f)
                                ),
                                startY = 0f,
                                endY = height
                            )
                        )
                    }

                    drawPath(
                        path = primaryPath,
                        color = primaryColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw Point dots
                    primaryPoints.forEachIndexed { i, pt ->
                        val animatedY = height - (height - pt.y) * animProgress.value
                        val isSelected = selectedIndex == i

                        if (isSelected) {
                            // Glowing halo
                            drawCircle(
                                color = primaryColor.copy(alpha = 0.25f),
                                radius = 9.dp.toPx(),
                                center = Offset(pt.x, animatedY)
                            )
                        }

                        drawCircle(
                            color = Color.White,
                            radius = if (isSelected) 5.dp.toPx() else 4.dp.toPx(),
                            center = Offset(pt.x, animatedY)
                        )
                        drawCircle(
                            color = primaryColor,
                            radius = if (isSelected) 4.dp.toPx() else 3.dp.toPx(),
                            center = Offset(pt.x, animatedY)
                        )
                    }
                }

                // 5. Draw Active Selection Vertical Line Indicator
                selectedIndex?.let { idx ->
                    val x = if (n > 1) idx * stepX else width / 2
                    drawLine(
                        color = primaryColor.copy(alpha = 0.6f),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )
                }
            }
        }

        // X-Axis Date Range Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val firstDate = metrics.firstOrNull()?.dateString ?: ""
            val midDate = metrics.getOrNull(metrics.size / 2)?.dateString ?: ""
            val lastDate = metrics.lastOrNull()?.dateString ?: ""

            Text(
                text = firstDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (metrics.size > 2 && midDate != firstDate && midDate != lastDate) {
                Text(
                    text = midDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "Today ($lastDate)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Interactive Bar Chart for Medication Adherence or Departmental Consultations
 */
@Composable
fun InteractiveHealthBarChart(
    categories: List<Pair<String, Int>>,
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFF00897B),
    unitLabel: String = "Visits",
    maxVal: Int = (categories.maxOfOrNull { it.second } ?: 10).coerceAtLeast(5)
) {
    var selectedBarIndex by remember { mutableStateOf<Int?>(null) }
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(categories) {
        selectedBarIndex = null
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, animationSpec = tween(600))
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(categories) {
                        detectTapGestures { offset ->
                            val n = categories.size
                            if (n > 0) {
                                val slotWidth = size.width / n
                                val idx = (offset.x / slotWidth).toInt().coerceIn(0, n - 1)
                                selectedBarIndex = idx
                            }
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val n = categories.size
                if (n == 0) return@Canvas

                val slotWidth = width / n
                val barWidth = slotWidth * 0.55f

                // Draw 3 horizontal gridlines
                for (i in 0..3) {
                    val gy = height * (i.toFloat() / 3)
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.25f),
                        start = Offset(0f, gy),
                        end = Offset(width, gy),
                        strokeWidth = 1f
                    )
                }

                categories.forEachIndexed { i, (label, value) ->
                    val isSelected = selectedBarIndex == i
                    val barHeight = ((value.toFloat() / maxVal) * height * animProgress.value).coerceIn(4f, height)
                    val left = i * slotWidth + (slotWidth - barWidth) / 2
                    val top = height - barHeight

                    val gradient = Brush.verticalGradient(
                        colors = if (isSelected) {
                            listOf(barColor, barColor.copy(alpha = 0.8f))
                        } else {
                            listOf(barColor.copy(alpha = 0.85f), barColor.copy(alpha = 0.55f))
                        }
                    )

                    drawRoundRect(
                        brush = gradient,
                        topLeft = Offset(left, top),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )

                    if (isSelected) {
                        drawRoundRect(
                            color = Color(0xFF1E88E5),
                            topLeft = Offset(left - 2, top - 2),
                            size = Size(barWidth + 4, barHeight + 4),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }
        }

        // Labels below bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categories.forEachIndexed { i, (label, value) ->
                val isSelected = selectedBarIndex == i
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "$value $unitLabel",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) barColor else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Clinical Triage & Risk Distribution Donut Visual
 */
@Composable
fun ClinicalTriageDonutChart(
    urgencyCounts: Map<String, Int>,
    totalSummaries: Int,
    modifier: Modifier = Modifier
) {
    val lowCount = urgencyCounts["Low"] ?: 0
    val modCount = urgencyCounts["Moderate"] ?: 0
    val highCount = (urgencyCounts["High"] ?: 0) + (urgencyCounts["Emergency"] ?: 0)
    val total = max(1, lowCount + modCount + highCount)

    val lowAngle = (lowCount.toFloat() / total) * 360f
    val modAngle = (modCount.toFloat() / total) * 360f
    val highAngle = (highCount.toFloat() / total) * 360f

    val lowColor = Color(0xFF43A047) // Green
    val modColor = Color(0xFFFB8C00) // Amber
    val highColor = Color(0xFFE53935) // Red

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Donut Canvas
        Box(
            modifier = Modifier.size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 18.dp.toPx()
                var startAngle = -90f

                // Draw Low segment
                if (lowAngle > 0f) {
                    drawArc(
                        color = lowColor,
                        startAngle = startAngle,
                        sweepAngle = lowAngle - 3f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    startAngle += lowAngle
                }

                // Draw Mod segment
                if (modAngle > 0f) {
                    drawArc(
                        color = modColor,
                        startAngle = startAngle,
                        sweepAngle = modAngle - 3f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    startAngle += modAngle
                }

                // Draw High segment
                if (highAngle > 0f) {
                    drawArc(
                        color = highColor,
                        startAngle = startAngle,
                        sweepAngle = highAngle - 3f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$total",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Summaries",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Breakdown Legend Column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TriageLegendRow(
                color = lowColor,
                label = "Routine / Low Triage",
                count = lowCount,
                percent = ((lowCount.toFloat() / total) * 100).toInt()
            )
            TriageLegendRow(
                color = modColor,
                label = "Moderate (Follow-up)",
                count = modCount,
                percent = ((modCount.toFloat() / total) * 100).toInt()
            )
            TriageLegendRow(
                color = highColor,
                label = "Priority Action",
                count = highCount,
                percent = ((highCount.toFloat() / total) * 100).toInt()
            )
        }
    }
}

@Composable
private fun TriageLegendRow(
    color: Color,
    label: String,
    count: Int,
    percent: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = "$count ($percent%)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Metric Summary Card with Range Slider Gauge
 */
@Composable
fun HealthMetricCard(
    title: String,
    latestValue: String,
    unit: String,
    statusText: String,
    statusColor: Color,
    contextTag: String,
    dateString: String,
    trendPercent: String? = null,
    isTrendingDown: Boolean = true,
    isGoodTrend: Boolean = true,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = latestValue,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                if (trendPercent != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                (if (isGoodTrend) Color(0xFF4CAF50) else Color(0xFFE53935)).copy(alpha = 0.1f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = if (isTrendingDown) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                            contentDescription = null,
                            tint = if (isGoodTrend) Color(0xFF2E7D32) else Color(0xFFC62828),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = trendPercent,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isGoodTrend) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Recorded on $dateString • $contextTag",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
