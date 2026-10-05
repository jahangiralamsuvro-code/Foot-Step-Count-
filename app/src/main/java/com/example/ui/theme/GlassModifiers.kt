package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(24.dp),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp,
    backgroundColor: Color? = null,
    borderColor: Color? = null
): Modifier {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val resolvedBg = backgroundColor ?: if (isDark) {
        MidnightGlassCard
    } else {
        LightGlassCard
    }
    val resolvedBorder = borderColor ?: if (isDark) {
        MidnightGlassBorder
    } else {
        LightGlassBorder
    }

    val gradientOverlay = Brush.verticalGradient(
        colors = if (isDark) listOf(
            Color(0x2EFFFFFF),
            Color(0x05FFFFFF),
            Color(0x1A00E5FF)
        ) else listOf(
            Color(0x66FFFFFF),
            Color(0x1AFFFFFF),
            Color(0x140091EA)
        )
    )

    return this
        .then(if (elevation > 0.dp) Modifier.shadow(elevation, shape) else Modifier)
        .clip(shape)
        .background(resolvedBg)
        .background(gradientOverlay)
        .border(borderWidth, resolvedBorder, shape)
}

@Composable
fun Modifier.elevatedLiquidGlass(
    shape: Shape = RoundedCornerShape(24.dp),
    elevation: Dp = 6.dp
): Modifier {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    return this.liquidGlass(
        shape = shape,
        borderWidth = 1.2.dp,
        elevation = elevation,
        backgroundColor = if (isDark) MidnightGlassCardElevated else LightGlassCardElevated,
        borderColor = if (isDark) MidnightGlassBorder else LightGlassBorder
    )
}
