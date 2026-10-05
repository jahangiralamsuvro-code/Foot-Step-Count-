package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import java.util.Locale

@Composable
fun DailyMetricsRow(
    distanceKm: Float,
    caloriesKcal: Int,
    activeMinutes: Int,
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MetricItemCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.NearMe,
            iconTint = CyanAccent,
            value = String.format(Locale.US, "%.1f", distanceKm),
            unit = "km",
            label = if (isBengali) "দূরত্ব" else "Distance",
            testTag = "metric_distance"
        )

        Spacer(modifier = Modifier.width(8.dp))

        MetricItemCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.LocalFireDepartment,
            iconTint = AmberAccent,
            value = caloriesKcal.toString(),
            unit = "kcal",
            label = if (isBengali) "ক্যালোরি" else "Calories",
            testTag = "metric_calories"
        )

        Spacer(modifier = Modifier.width(8.dp))

        MetricItemCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Timer,
            iconTint = MintAccent,
            value = activeMinutes.toString(),
            unit = "min",
            label = if (isBengali) "সক্রিয় সময়" else "Active Time",
            testTag = "metric_active_time"
        )
    }
}

@Composable
fun MetricItemCard(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    unit: String,
    label: String,
    modifier: Modifier = Modifier,
    testTag: String = "metric_item"
) {
    LiquidGlassCard(
        modifier = modifier.testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        borderWidth = 1.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
