package com.example.ui.screens.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyActivityRecord
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HistoryScreen(
    historyList: List<DailyActivityRecord>,
    isBengali: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedView by remember { mutableIntStateOf(0) } // 0=Calendar, 1=List
    var selectedDayNumber by remember { mutableIntStateOf(12) }

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Match mockup screen 9 calendar grid (August 2025)
    val calendarDays = (1..31).toList()

    val selectedRecord = historyList.firstOrNull() ?: DailyActivityRecord(
        date = "2025-08-12",
        steps = 6248,
        goal = 10000,
        distanceKm = 4.5f,
        caloriesBurned = 312,
        activeMinutes = 48
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("history_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("history_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "হাঁটার ইতিহাস" else "History",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Toggle View: Calendar vs List
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x1F00E5FF))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedView == 0) CyanAccent else Color.Transparent)
                    .clickable { selectedView = 0 }
                    .testTag("history_calendar_tab"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBengali) "ক্যালেন্ডার" else "Calendar",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (selectedView == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedView == 0) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedView == 1) CyanAccent else Color.Transparent)
                    .clickable { selectedView = 1 }
                    .testTag("history_list_tab"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBengali) "তালিকা" else "List",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (selectedView == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedView == 1) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedView == 0) {
            // Calendar Month Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Prev Month",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "August 2025",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Month",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Days of week row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Calendar Grid of 31 days
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    val rows = calendarDays.chunked(7)
                    rows.forEach { week ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            week.forEach { dayNum ->
                                val isSelected = dayNum == selectedDayNumber
                                val isHighActivity = dayNum in listOf(6, 12, 18, 20, 24)
                                val isMediumActivity = dayNum in listOf(3, 8, 14, 17, 28)

                                val badgeBg = when {
                                    isSelected -> CyanAccent
                                    isHighActivity -> MintAccent.copy(alpha = 0.35f)
                                    isMediumActivity -> Color(0x3300E5FF)
                                    else -> Color.Transparent
                                }

                                val textColor = when {
                                    isSelected -> Color(0xFF00363A)
                                    isHighActivity -> MintAccent
                                    isMediumActivity -> CyanAccent
                                    else -> MaterialTheme.colorScheme.onBackground
                                }

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(badgeBg)
                                        .then(
                                            if (isSelected) Modifier.border(2.dp, Color.White, CircleShape)
                                            else Modifier
                                        )
                                        .clickable { selectedDayNumber = dayNum },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$dayNum",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (isSelected || isHighActivity) FontWeight.Bold else FontWeight.Normal,
                                            color = textColor
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendDot(color = CyanAccent, label = if (isBengali) "কম" else "Low")
                        Spacer(modifier = Modifier.width(16.dp))
                        LegendDot(color = MintAccent, label = if (isBengali) "মাঝারি" else "Medium")
                        Spacer(modifier = Modifier.width(16.dp))
                        LegendDot(color = AmberAccent, label = if (isBengali) "লক্ষ্য পূর্ণ" else "Goal Met")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected Date Detail Card (Matches mockup screen 9)
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("history_detail_card"),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "$selectedDayNumber Aug 2025",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailChip(
                            icon = Icons.Default.Timeline,
                            tint = CyanAccent,
                            value = "${NumberFormat.getNumberInstance(Locale.US).format(selectedRecord.steps)} steps",
                            label = if (isBengali) "কদম" else "Steps"
                        )
                        DetailChip(
                            icon = Icons.Default.NearMe,
                            tint = MintAccent,
                            value = "${String.format(Locale.US, "%.1f", selectedRecord.distanceKm)} km",
                            label = if (isBengali) "দূরত্ব" else "Distance"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailChip(
                            icon = Icons.Default.LocalFireDepartment,
                            tint = AmberAccent,
                            value = "${selectedRecord.caloriesBurned} kcal",
                            label = if (isBengali) "ক্যালোরি" else "Calories"
                        )
                        DetailChip(
                            icon = Icons.Default.Timer,
                            tint = CyanAccent,
                            value = "${selectedRecord.activeMinutes} min",
                            label = if (isBengali) "সক্রিয় সময়" else "Active Time"
                        )
                    }
                }
            }
        } else {
            // List View
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(historyList) { record ->
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = record.date,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                                Text(
                                    text = "${NumberFormat.getNumberInstance(Locale.US).format(record.steps)} steps / ${record.goal}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyanAccent
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${String.format(Locale.US, "%.1f", record.distanceKm)} km",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MintAccent
                                    )
                                )
                                Text(
                                    text = "${record.caloriesBurned} kcal",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun DetailChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    value: String,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
