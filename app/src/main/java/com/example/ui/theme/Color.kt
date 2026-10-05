package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Midnight Liquid Glass Palette
val MidnightDarkBg = Color(0xFF090E1A)
val MidnightDarkSurface = Color(0xFF0F182B)
val MidnightGlassCard = Color(0xCC14213B)
val MidnightGlassCardElevated = Color(0xDD1A2B4C)
val MidnightGlassBorder = Color(0x3340C4FF)
val MidnightGlassBorderMuted = Color(0x1F80D8FF)

// Light Mode Liquid Glass Palette
val LightBg = Color(0xFFF3F6FA)
val LightSurface = Color(0xFFFFFFFF)
val LightGlassCard = Color(0xE6FFFFFF)
val LightGlassCardElevated = Color(0xF5FFFFFF)
val LightGlassBorder = Color(0x260091EA)
val LightGlassBorderMuted = Color(0x140091EA)

// Vibrant Accent Colors
val CyanAccent = Color(0xFF00E5FF)
val MintAccent = Color(0xFF00E676)
val BlueAccent = Color(0xFF2979FF)
val PurpleAccent = Color(0xFF7C4DFF)
val AmberAccent = Color(0xFFFFB300)
val OrangeAccent = Color(0xFFFF6D00)
val RoseAccent = Color(0xFFFF4081)
val CoralRed = Color(0xFFFF5252)

// Text Colors
val TextPrimaryDark = Color(0xFFF1F5F9)
val TextSecondaryDark = Color(0xFF94A3B8)
val TextMutedDark = Color(0xFF64748B)

val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF475569)
val TextMutedLight = Color(0xFF94A3B8)

// Gradients
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(CyanAccent, BlueAccent)
)

val MintGradient = Brush.linearGradient(
    colors = listOf(CyanAccent, MintAccent)
)

val PurpleGradient = Brush.linearGradient(
    colors = listOf(BlueAccent, PurpleAccent)
)

val CalorieGradient = Brush.linearGradient(
    colors = listOf(AmberAccent, OrangeAccent)
)

val GlassSurfaceGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x332A3E66),
        Color(0x1A14223A)
    )
)
