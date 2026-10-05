package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CyanAccent,
    onPrimary = Color(0xFF00363A),
    primaryContainer = Color(0xFF004D40),
    onPrimaryContainer = Color(0xFFE0F7FA),
    secondary = MintAccent,
    onSecondary = Color(0xFF003816),
    secondaryContainer = Color(0xFF005322),
    onSecondaryContainer = Color(0xFFB9F6CA),
    tertiary = PurpleAccent,
    onTertiary = Color.White,
    background = MidnightDarkBg,
    onBackground = TextPrimaryDark,
    surface = MidnightDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = MidnightGlassCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = MidnightGlassBorder,
    outlineVariant = MidnightGlassBorderMuted,
    error = CoralRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BlueAccent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = MintAccent,
    onSecondary = Color(0xFF003816),
    secondaryContainer = Color(0xFFE8F5E9),
    onSecondaryContainer = Color(0xFF1B5E20),
    tertiary = PurpleAccent,
    onTertiary = Color.White,
    background = LightBg,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightGlassCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightGlassBorder,
    outlineVariant = LightGlassBorderMuted,
    error = CoralRed,
    onError = Color.White
)

@Composable
fun VitalStepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
