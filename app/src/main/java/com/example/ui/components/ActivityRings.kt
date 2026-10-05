package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ActivityRing(
    steps: Int,
    goal: Int,
    modifier: Modifier = Modifier,
    size: Dp = 250.dp,
    strokeWidth: Dp = 16.dp,
    testTag: String = "activity_ring"
) {
    val progressFraction = if (goal > 0) (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "ring_progress"
    )

    val sweepAngle = animatedProgress * 360f
    val percentageInt = (progressFraction * 100).toInt()
    val formattedSteps = NumberFormat.getNumberInstance(Locale.US).format(steps)
    val formattedGoal = NumberFormat.getNumberInstance(Locale.US).format(goal)

    Box(
        modifier = modifier
            .size(size)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val diameter = this.size.minDimension - strokePx
            val radius = diameter / 2f
            val centerOffset = Offset(this.size.width / 2f, this.size.height / 2f)

            // Background Track
            drawCircle(
                color = Color(0x1F00E5FF),
                radius = radius,
                center = centerOffset,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Progress Arc
            if (sweepAngle > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            CyanAccent,
                            MintAccent,
                            CyanAccent
                        ),
                        center = centerOffset
                    ),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }
        }

        // Center Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsWalk,
                contentDescription = "Steps",
                tint = CyanAccent,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formattedSteps,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Text(
                text = "/ $formattedGoal steps",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$percentageInt% completed",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MintAccent,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
