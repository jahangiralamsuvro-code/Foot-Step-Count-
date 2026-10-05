package com.example.ui.screens.activity

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
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
import com.example.data.model.DailyStepGoalProgress
import com.example.data.model.UserProfile
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.StepGoalTrackerCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityScreen(
    todayRecord: DailyActivityRecord?,
    historyList: List<DailyActivityRecord>,
    userProfile: UserProfile = UserProfile(),
    isBengali: Boolean,
    onStepGoalChange: (Int) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0=Day, 1=Week, 2=Month, 3=Year
    val tabTitlesEn = listOf("Day", "Week", "Month", "Year")
    val tabTitlesBn = listOf("দিন", "সপ্তাহ", "মাস", "বছর")

    val steps = todayRecord?.steps ?: 6248
    val distance = todayRecord?.distanceKm ?: 4.5f
    val calories = todayRecord?.caloriesBurned ?: 312
    val activeMinutes = todayRecord?.activeMinutes ?: 48

    val dateFormatted = SimpleDateFormat("d MMM yyyy", Locale.US).format(Date())

    // Hourly mock distribution for today (6am, 9am, 12pm, 3pm, 6pm, 9pm)
    val hourlyData = listOf(
        "6am" to 420,
        "9am" to 1350,
        "12pm" to 890,
        "3pm" to 710,
        "6pm" to 1840,
        "9pm" to 1038
    )
    val maxHourlySteps = 2200f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("activity_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x2200E5FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = CyanAccent,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (isBengali) "দৈনিক অ্যাক্টিভিটি" else "Activity",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Time Range Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x1F00E5FF))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabTitlesEn.indices.forEach { index ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) CyanAccent else Color.Transparent)
                        .clickable { selectedTab = index }
                        .testTag("activity_tab_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBengali) tabTitlesBn[index] else tabTitlesEn[index],
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Date Navigator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = dateFormatted,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hourly Activity Bar Chart
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .testTag("hourly_chart_card"),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = if (isBengali) "ঘন্টায় ঘন্টায় কদম বিশ্লেষণ" else "Hourly Steps Breakdown",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyanAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    hourlyData.forEach { (timeLabel, value) ->
                        val barHeightFraction = (value / maxHourlySteps).coerceIn(0.1f, 1f)
                        val animHeight by animateFloatAsState(
                            targetValue = barHeightFraction,
                            animationSpec = tween(800, easing = FastOutSlowInEasing),
                            label = "bar_$timeLabel"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height((110 * animHeight).dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(CyanAccent)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = timeLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Step Goal Tracker with Progress Bar Indicator
        val stepGoalProgress = remember(steps, todayRecord?.goal, userProfile.heightCm, userProfile.currentWeightKg) {
            DailyStepGoalProgress.fromActivityAndProfile(todayRecord, userProfile)
        }
        StepGoalTrackerCard(
            goalProgress = stepGoalProgress,
            onGoalChange = onStepGoalChange,
            isBengali = isBengali
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Summary Card
        Text(
            text = if (isBengali) "দিনের সারসংক্ষেপ" else "Daily Summary",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("daily_summary_card"),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryMetric(
                        icon = Icons.Default.Timeline,
                        tint = CyanAccent,
                        value = NumberFormat.getNumberInstance(Locale.US).format(steps),
                        label = if (isBengali) "কদম (Steps)" else "Steps"
                    )

                    SummaryMetric(
                        icon = Icons.Default.NearMe,
                        tint = MintAccent,
                        value = "${String.format(Locale.US, "%.1f", distance)} km",
                        label = if (isBengali) "দূরত্ব" else "Distance"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryMetric(
                        icon = Icons.Default.LocalFireDepartment,
                        tint = AmberAccent,
                        value = "$calories kcal",
                        label = if (isBengali) "ক্যালোরি" else "Calories"
                    )

                    SummaryMetric(
                        icon = Icons.Default.Timer,
                        tint = CyanAccent,
                        value = "$activeMinutes min",
                        label = if (isBengali) "সক্রিয় সময়" else "Active Time"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Weekly Progress Ring Row (M, T, W, T, F, S, S)
        Text(
            text = if (isBengali) "সাপ্তাহিক অগ্রগতি" else "Weekly Progress",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("weekly_progress_card"),
            shape = RoundedCornerShape(22.dp)
        ) {
            val days = listOf("M", "T", "W", "T", "F", "S", "S")
            val dayPercents = listOf(85, 92, 100, 68, 95, 80, 62)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                days.indices.forEach { idx ->
                    val dayName = days[idx]
                    val pct = dayPercents[idx]
                    val isDone = pct >= 80

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isDone) MintAccent.copy(alpha = 0.25f) else Color(0x1F00E5FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${pct}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDone) MintAccent else CyanAccent
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = dayName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Lifetime Statistics Card
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("lifetime_stats_card"),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = AmberAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBengali) "সার্বক্ষণিক রেকর্ড ও পরিসংখ্যান" else "Lifetime Statistics",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isBengali) "মোট কদম" else "Total Steps",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = "52,480",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        )
                    }

                    Column {
                        Text(
                            text = if (isBengali) "সর্বোচ্চ একদিনের কদম" else "Best Day",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = "10,250",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MintAccent
                            )
                        )
                    }

                    Column {
                        Text(
                            text = if (isBengali) "মোট দূরত্ব" else "Total Distance",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = "38.2 km",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AmberAccent
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun SummaryMetric(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    value: String,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
