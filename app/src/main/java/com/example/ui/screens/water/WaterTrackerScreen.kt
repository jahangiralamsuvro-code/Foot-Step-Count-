package com.example.ui.screens.water

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import java.util.Locale

@Composable
fun WaterTrackerScreen(
    currentWaterMl: Int,
    dailyTargetMl: Int,
    isBengali: Boolean,
    onAddWater: (Int) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val currentLiters = currentWaterMl / 1000f
    val targetLiters = dailyTargetMl / 1000f
    val progressFraction = (currentWaterMl.toFloat() / dailyTargetMl.toFloat()).coerceIn(0f, 1f)
    val animProgress by animateFloatAsState(targetValue = progressFraction, label = "water_progress")
    val percentageInt = (progressFraction * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("water_tracker_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("water_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "পানি ট্র্যাকার" else "Water Tracker",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Large Hydration Gauge (Matching mockup screen 10)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .testTag("water_gauge_card"),
            shape = RoundedCornerShape(32.dp),
            borderColor = CyanAccent.copy(alpha = 0.4f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Water Drop Glowing Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0x3300E5FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Water",
                        tint = CyanAccent,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "${String.format(Locale.US, "%.1f", currentLiters)} L",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 42.sp
                    )
                )

                Text(
                    text = "of ${String.format(Locale.US, "%.1f", targetLiters)} L",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = CyanAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Minus & Plus Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0x2200E5FF))
                            .clickable {
                                if (currentWaterMl >= 250) onAddWater(-250)
                            }
                            .testTag("water_minus_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove 250ml",
                            tint = CyanAccent,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(36.dp))

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(CyanAccent)
                            .clickable { onAddWater(250) }
                            .testTag("water_plus_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add 250ml",
                            tint = Color(0xFF00363A),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Quick Add Glass Cup Row (Matching mockup screen 10)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val cups = listOf(250, 250, 250, 250, 250)
            cups.indices.forEach { idx ->
                val amount = cups[idx]
                GlassCupItem(
                    amountMl = amount,
                    onClick = { onAddWater(amount) },
                    modifier = Modifier.weight(1f),
                    tag = "cup_$idx"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Progress Bar Card
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("water_progress_card"),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isBengali) "আজকের অগ্রগতি" else "Today's Progress",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(
                        text = "$percentageInt%",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { animProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = CyanAccent,
                    trackColor = Color(0x3300E5FF)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Hydration Tips Card
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isBengali) "পানি পানের স্বাস্থ্যবার্তা" else "Smart Hydration Advice",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MintAccent
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isBengali)
                        "নিয়মিত হাঁটার সময় প্রতি ২০ মিনিটে ১৫০-২০০ মিলি পানি পান করুন। কিডনি রোগ বা হৃদরোগের কারণে চিকিৎসকের পানি গ্রহণের সীমা থাকলে চিকিৎসকের পরামর্শ মেনে চলুন।"
                    else
                        "Drink 150-200ml every 20 minutes of active walking. Users with medical fluid restrictions should adhere strictly to their physician's guidance.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun GlassCupItem(
    amountMl: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x2200E5FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalDrink,
                contentDescription = "+$amountMl ml",
                tint = CyanAccent,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$amountMl ml",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
