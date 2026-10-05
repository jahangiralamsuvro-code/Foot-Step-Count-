package com.example.ui.screens.weight

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.WeightRecord
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import java.util.Locale
import kotlin.math.abs

@Composable
fun WeightTrackerScreen(
    weightRecords: List<WeightRecord>,
    userProfile: UserProfile,
    isBengali: Boolean,
    onRecordWeight: (Float, String) -> Unit,
    onDeleteWeight: (Long) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var showAddDialog by remember { mutableStateOf(false) }
    var inputWeightStr by remember { mutableStateOf(userProfile.currentWeightKg.toString()) }
    var inputNote by remember { mutableStateOf("") }

    val currentWeight = weightRecords.lastOrNull()?.weightKg ?: userProfile.currentWeightKg
    val targetWeight = userProfile.targetWeightKg
    val diff = currentWeight - targetWeight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("weight_tracker_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("weight_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "ওজন ট্র্যাকার" else "Weight Tracker",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Current vs Target Weight Card (Matching mockup screen 11)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("weight_summary_card"),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isBengali) "বর্তমান ওজন" else "Current Weight",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = String.format(Locale.US, "%.1f", currentWeight),
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "kg",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = CyanAccent,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (isBengali) "লক্ষ্য: ${targetWeight.toInt()} কেজি" else "Target: ${targetWeight.toInt()} kg",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MintAccent
                            )
                        )
                        Text(
                            text = if (diff > 0) "-${String.format(Locale.US, "%.1f", diff)} kg to go" else "Goal Achieved!",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyanAccent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Weight Trend Line Graph
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Grid lines
                        val yLines = listOf(0.2f, 0.5f, 0.8f)
                        yLines.forEach { yRatio ->
                            drawLine(
                                color = Color(0x1F00E5FF),
                                start = Offset(0f, h * yRatio),
                                end = Offset(w, h * yRatio),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        // Sample curve coordinates
                        val points = listOf(
                            Offset(w * 0.1f, h * 0.25f),
                            Offset(w * 0.35f, h * 0.40f),
                            Offset(w * 0.65f, h * 0.60f),
                            Offset(w * 0.9f, h * 0.70f)
                        )

                        val path = Path()
                        points.forEachIndexed { i, pt ->
                            if (i == 0) path.moveTo(pt.x, pt.y)
                            else path.lineTo(pt.x, pt.y)
                        }

                        drawPath(
                            path = path,
                            color = CyanAccent,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        points.forEach { pt ->
                            drawCircle(color = CyanAccent, radius = 5.dp.toPx(), center = pt)
                            drawCircle(color = Color.White, radius = 2.dp.toPx(), center = pt)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val months = listOf("Jul", "Aug", "Sep", "Oct")
                    months.forEach { m ->
                        Text(
                            text = m,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Record Weight Primary Button
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("record_weight_button"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF00363A)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isBengali) "নতুন ওজন রেকর্ড করুন" else "Record Weight",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00363A)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Weight Log List
        Text(
            text = if (isBengali) "ওজন পরিবর্তনের তালিকা" else "Weight History Log",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        weightRecords.reversed().forEach { rec ->
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${rec.weightKg} kg",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        )
                        Text(
                            text = "${rec.date} ${if (rec.note.isNotEmpty()) "• ${rec.note}" else ""}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    IconButton(onClick = { onDeleteWeight(rec.id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete entry",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(if (isBengali) "ওজন রেকর্ড করুন" else "Record New Weight") },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputWeightStr,
                        onValueChange = { inputWeightStr = it },
                        label = { Text("Weight (kg)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = inputNote,
                        onValueChange = { inputNote = it },
                        label = { Text("Note (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = inputWeightStr.toFloatOrNull() ?: currentWeight
                        onRecordWeight(w, inputNote)
                        showAddDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
