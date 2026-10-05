package com.example.ui.screens.bmi

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.UserProfile
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun BmiCalculatorScreen(
    userProfile: UserProfile,
    isBengali: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var isMetric by remember { mutableStateOf(true) }
    var heightCm by remember { mutableFloatStateOf(userProfile.heightCm) }
    var weightKg by remember { mutableFloatStateOf(userProfile.currentWeightKg) }

    // BMI Calculation
    val heightM = heightCm / 100f
    val bmi = if (heightM > 0f) weightKg / (heightM * heightM) else 0f
    val formattedBmi = String.format(Locale.US, "%.1f", bmi)

    val (bmiCategory, categoryColor) = when {
        bmi < 18.5f -> (if (isBengali) "স্বাভাবিকের চেয়ে কম" else "Underweight") to AmberAccent
        bmi < 25.0f -> (if (isBengali) "স্বাভাবিক (Normal)" else "Normal") to MintAccent
        bmi < 30.0f -> (if (isBengali) "অতিরিক্ত ওজন (Overweight)" else "Overweight") to AmberAccent
        else -> (if (isBengali) "স্থূলতা (Obese)" else "Obese") to Color(0xFFFF5252)
    }

    val healthyMinKg = 18.5f * (heightM * heightM)
    val healthyMaxKg = 24.9f * (heightM * heightM)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("bmi_calculator_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("bmi_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "বিএমআই ক্যালকুলেটর" else "BMI Calculator",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metric / Imperial Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x1F00E5FF))
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isMetric) CyanAccent else Color.Transparent)
                    .clickable { isMetric = true }
                    .testTag("bmi_metric_toggle"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Metric (cm / kg)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isMetric) FontWeight.Bold else FontWeight.Normal,
                        color = if (isMetric) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (!isMetric) CyanAccent else Color.Transparent)
                    .clickable { isMetric = false }
                    .testTag("bmi_imperial_toggle"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Imperial (in / lbs)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (!isMetric) FontWeight.Bold else FontWeight.Normal,
                        color = if (!isMetric) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Height Input Slider
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bmi_height_card"),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBengali) "উচ্চতা (Height)" else "Height",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Text(
                        text = if (isMetric) "${heightCm.roundToInt()} cm" else "${(heightCm / 2.54f).roundToInt()} in",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = heightCm,
                    onValueChange = { heightCm = it },
                    valueRange = 120f..220f,
                    colors = SliderDefaults.colors(
                        thumbColor = CyanAccent,
                        activeTrackColor = CyanAccent,
                        inactiveTrackColor = Color(0x3300E5FF)
                    ),
                    modifier = Modifier.testTag("height_slider")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weight Input Slider
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bmi_weight_card"),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBengali) "ওজন (Weight)" else "Weight",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Text(
                        text = if (isMetric) "${weightKg.roundToInt()} kg" else "${(weightKg * 2.20462f).roundToInt()} lbs",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MintAccent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = weightKg,
                    onValueChange = { weightKg = it },
                    valueRange = 35f..150f,
                    colors = SliderDefaults.colors(
                        thumbColor = MintAccent,
                        activeTrackColor = MintAccent,
                        inactiveTrackColor = Color(0x3300E676)
                    ),
                    modifier = Modifier.testTag("weight_slider")
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BMI Result Card (Matches mockup screen 8)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("bmi_result_card"),
            shape = RoundedCornerShape(28.dp),
            borderColor = categoryColor.copy(alpha = 0.5f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isBengali) "আপনার বিএমআই" else "Your BMI",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Circular Dial Visual
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0x1F00E5FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formattedBmi,
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = categoryColor,
                                fontSize = 38.sp
                            )
                        )
                        Text(
                            text = bmiCategory,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = categoryColor
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isBengali) "স্বাভাবিক প্রাপ্তবয়স্ক মাত্রা: ১৮.৫ - ২৪.৯" else "Healthy Adult Range: 18.5 - 24.9",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isBengali)
                        "আপনার উচ্চতায় সুস্থ স্বাভাবিক ওজনের পরিসীমা: ${String.format(Locale.US, "%.1f", healthyMinKg)} - ${String.format(Locale.US, "%.1f", healthyMaxKg)} কেজি।"
                    else
                        "Estimated healthy weight for your height: ${String.format(Locale.US, "%.1f", healthyMinKg)} - ${String.format(Locale.US, "%.1f", healthyMaxKg)} kg.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFB0BEC5)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Medical Disclaimer
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            backgroundColor = Color(0x1F64748B)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF90A4AE),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBengali)
                        "বিএমআই প্রাপ্তবয়স্কদের ক্ষেত্রে শারীরিক স্থূলতার একটি সাধারণ স্ক্রিনিং পদ্ধতি। এটি কোনো সরাসরি রোগ নির্ণয় নয় এবং গর্ভবতী নারী, ক্রীড়াবিদ বা অপ্রাপ্তবয়স্কদের ক্ষেত্রে ভিন্ন হতে পারে।"
                    else
                        "BMI is a screening tool for adults, not a medical diagnosis. Individual body composition, muscle mass, age, and health history should be evaluated by a healthcare professional.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFFB0BEC5),
                        lineHeight = 16.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
