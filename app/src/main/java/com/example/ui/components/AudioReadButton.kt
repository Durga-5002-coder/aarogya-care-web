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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MedicalTealDark
import com.example.ui.theme.MedicalTealPrimary

@Composable
fun AudioReadButton(
    isCurrentlySpeaking: Boolean,
    onSpeak: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    testTag: String = "audio_read_button"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isCurrentlySpeaking) Color(0xFFFFEBEE) else Color(0xFFE0F2F1),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.5.dp,
            color = if (isCurrentlySpeaking) Color(0xFFEF4444) else MedicalTealPrimary
        ),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                if (isCurrentlySpeaking) onStop() else onSpeak()
            }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .scale(if (isCurrentlySpeaking) pulseScale else 1f)
                    .background(
                        color = if (isCurrentlySpeaking) Color(0xFFEF4444) else MedicalTealDark,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCurrentlySpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                    contentDescription = if (isCurrentlySpeaking) "Stop Voice" else "Listen Audio",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            if (!label.isNullOrBlank()) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isCurrentlySpeaking) "ఆపు / Stop" else label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentlySpeaking) Color(0xFFB91C1C) else MedicalTealDark
                )
            }
        }
    }
}
