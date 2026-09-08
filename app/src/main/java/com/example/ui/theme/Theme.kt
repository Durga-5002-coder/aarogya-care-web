package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MedicalTealPrimary,
    onPrimary = SurfaceLight,
    primaryContainer = MedicalTealDark,
    onPrimaryContainer = MedicalTealLight,
    secondary = MedicalBluePrimary,
    onSecondary = SurfaceLight,
    secondaryContainer = MedicalBlueDark,
    onSecondaryContainer = MedicalBlueLight,
    tertiary = MedicalAccentAmber,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceCardDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = OutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = MedicalTealPrimary,
    onPrimary = SurfaceLight,
    primaryContainer = MedicalTealContainer,
    onPrimaryContainer = MedicalTealDark,
    secondary = MedicalBluePrimary,
    onSecondary = SurfaceLight,
    secondaryContainer = MedicalBlueLight,
    onSecondaryContainer = MedicalBlueDark,
    tertiary = MedicalAccentAmber,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceCard,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineLight
)

@Composable
fun AarogyaCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AarogyaCareTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}


