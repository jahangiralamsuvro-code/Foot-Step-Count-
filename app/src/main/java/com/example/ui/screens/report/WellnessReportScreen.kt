package com.example.ui.screens.report

import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
fun WellnessReportScreen(
    historyList: List<DailyActivityRecord>,
    isBengali: Boolean,
    onExportCsv: ((String) -> Unit) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0=Weekly, 1=Monthly, 2=Yearly
    val tabTitlesEn = listOf("Weekly", "Monthly", "Yearly")
    val tabTitlesBn = listOf("সাপ্তাহিক", "মাসিক", "বার্ষিক")

    // Weekly summary numbers matching mockup screen 14
    val totalSteps = 42560
    val avgSteps = 6080
    val totalDistance = 31.2f
    val totalCalories = 2140

    val weeklyBars = listOf(
        "Mon" to 5800,
        "Tue" to 7100,
        "Wed" to 6400,
        "Thu" to 8200,
        "Fri" to 5400,
        "Sat" to 4900,
        "Sun" to 4760
    )
    val maxStep = 9000f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("wellness_report_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("report_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "ওয়েলনেস রিপোর্ট" else "Wellness Report",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) CyanAccent else Color.Transparent)
                        .clickable { selectedTab = index }
                        .testTag("report_tab_$index"),
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

        // Date Interval Header (Matches mockup)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Prev",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "5 Aug - 11 Aug 2025",
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

        // Bar Chart Card
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .testTag("report_chart_card"),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = if (isBengali) "সাপ্তাহিক কদম বিশ্লেষণ" else "Weekly Step Consistency",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyanAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyBars.forEach { (dayLabel, value) ->
                        val barHeightFraction = (value / maxStep).coerceIn(0.15f, 1f)
                        val animHeight by animateFloatAsState(
                            targetValue = barHeightFraction,
                            animationSpec = tween(900, easing = FastOutSlowInEasing),
                            label = "bar_$dayLabel"
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
                                text = dayLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2x2 Metric Cards Grid (Matching mockup screen 14)
        Row(modifier = Modifier.fillMaxWidth()) {
            ReportMetricCard(
                icon = Icons.Default.Timeline,
                tint = CyanAccent,
                value = NumberFormat.getNumberInstance(Locale.US).format(totalSteps),
                label = if (isBengali) "মোট কদম" else "Total Steps",
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            ReportMetricCard(
                icon = Icons.Default.TrendingUp,
                tint = MintAccent,
                value = NumberFormat.getNumberInstance(Locale.US).format(avgSteps),
                label = if (isBengali) "গড় কদম" else "Avg Steps",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            ReportMetricCard(
                icon = Icons.Default.NearMe,
                tint = CyanAccent,
                value = "${String.format(Locale.US, "%.1f", totalDistance)} km",
                label = if (isBengali) "মোট দূরত্ব" else "Distance",
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            ReportMetricCard(
                icon = Icons.Default.LocalFireDepartment,
                tint = AmberAccent,
                value = "${NumberFormat.getNumberInstance(Locale.US).format(totalCalories)} kcal",
                label = if (isBengali) "ক্যালোরি ব্যয়" else "Calories",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Export CSV Button
        Button(
            onClick = {
                onExportCsv { csvContent ->
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, csvContent)
                        type = "text/csv"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Export VitalStep Records"))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("export_csv_button"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
        ) {
            Icon(
                imageVector = Icons.Default.FileDownload,
                contentDescription = null,
                tint = Color(0xFF00363A)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isBengali) "সিএসভি এক্সপোর্ট করুন (Export CSV)" else "Export CSV Report",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00363A)
                )
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun ReportMetricCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier.height(105.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}
