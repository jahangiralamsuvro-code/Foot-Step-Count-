package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.liquidGlass

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    testTag: String = "liquid_glass_card",
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true, color = Color(0x3300E5FF)),
            onClick = onClick
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .liquidGlass(
                shape = shape,
                borderWidth = borderWidth,
                elevation = elevation,
                backgroundColor = backgroundColor,
                borderColor = borderColor
            )
            .then(clickModifier)
            .padding(16.dp),
        content = content
    )
}
