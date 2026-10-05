package com.example.ui.screens.exercise

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ExerciseItem
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent

@Composable
fun ExerciseGuideScreen(
    exerciseList: List<ExerciseItem>,
    isBengali: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedTab by remember { mutableStateOf("Beginner") }
    var activeExerciseModal by remember { mutableStateOf<ExerciseItem?>(null) }

    val categories = listOf("Beginner", "Walking", "Stretching")
    val categoriesBn = listOf("নতুনদের", "হাঁটা", "স্ট্রেচিং")

    val filteredList = exerciseList.filter {
        it.category.equals(selectedTab, ignoreCase = true) || selectedTab == "Beginner"
    }

    val featuredWorkout = exerciseList.find { it.category == "Walking" } ?: exerciseList.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("exercise_guide_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("exercise_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "ব্যায়াম ও হাঁটার গাইড" else "Exercise Guide",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x1F00E5FF))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            categories.indices.forEach { idx ->
                val cat = categories[idx]
                val isSelected = selectedTab == cat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) CyanAccent else Color.Transparent)
                        .clickable { selectedTab = cat }
                        .testTag("exercise_tab_$cat"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBengali) categoriesBn[idx] else cat,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Featured Exercise Card (Matching mockup screen 12)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("featured_exercise_card"),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.img_brisk_walking),
                    contentDescription = "Brisk Walking",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isBengali) featuredWorkout.titleBn else featuredWorkout.titleEn,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = CyanAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${featuredWorkout.durationMin} min",
                        style = MaterialTheme.typography.labelMedium.copy(color = CyanAccent)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MintAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = featuredWorkout.difficulty,
                        style = MaterialTheme.typography.labelMedium.copy(color = MintAccent)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isBengali) featuredWorkout.summaryBn else featuredWorkout.summaryEn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { activeExerciseModal = featuredWorkout },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("start_guide_button"),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color(0xFF00363A)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBengali) "গাইড শুরু করুন" else "Start Guide",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00363A)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // All Exercises List
        Text(
            text = if (isBengali) "ব্যায়াম সমূহ" else "All Workouts",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        filteredList.forEach { exercise ->
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(18.dp),
                onClick = { activeExerciseModal = exercise }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x3300E5FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBengali) exercise.titleBn else exercise.titleEn,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = "${exercise.durationMin} min • ${exercise.difficulty}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyanAccent
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }

    // Step-by-Step Exercise Modal
    activeExerciseModal?.let { item ->
        AlertDialog(
            onDismissRequest = { activeExerciseModal = null },
            title = {
                Text(
                    text = if (isBengali) item.titleBn else item.titleEn,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = if (isBengali) item.summaryBn else item.summaryEn,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isBengali) "ধাপসমূহ:" else "Steps:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    )

                    val steps = if (isBengali) item.stepsListBn else item.stepsListEn
                    steps.forEachIndexed { i, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = "${i + 1}. ",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintAccent
                                )
                            )
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = AmberAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBengali) item.safetyTipsBn else item.safetyTipsEn,
                            style = MaterialTheme.typography.labelSmall.copy(color = AmberAccent)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { activeExerciseModal = null }
                ) {
                    Text(if (isBengali) "সম্পন্ন" else "Got It")
                }
            }
        )
    }
}
