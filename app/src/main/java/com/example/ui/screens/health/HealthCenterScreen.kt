package com.example.ui.screens.health

import android.content.Intent
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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.HealthTip
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseAccent
import com.example.ui.viewmodel.Screen

@Composable
fun HealthCenterScreen(
    tipsList: List<HealthTip>,
    isBengali: Boolean,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("Today") }
    var isFav by remember { mutableStateOf(false) }

    val tabs = listOf("Today", "Morning", "Food", "Exercise")
    val tabsBn = listOf("আজকের", "সকালের", "খাবার", "ব্যায়াম")

    val featuredTip = tipsList.firstOrNull() ?: HealthTip(
        id = 1,
        titleEn = "Start With Warm Water",
        titleBn = "আজকের পরামর্শ",
        contentEn = "Start your day with a glass of warm water to boost metabolism.",
        contentBn = "সকালে গরম পানি পান করলে শরীরের বিপাক ক্রিয়া ভালোভাবে কাজ করে।",
        category = "Hydration",
        timeOfDay = "Morning"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("health_center_screen")
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
                    .background(Color(0x33FF4081)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = RoseAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (isBengali) "স্বাস্থ্যকথা ও পরামর্শ" else "Health Tips",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs (Today, Morning, Food, Exercise)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x1F00E5FF))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.indices.forEach { idx ->
                val tab = tabs[idx]
                val isSelected = selectedTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) CyanAccent else Color.Transparent)
                        .clickable { selectedTab = tab }
                        .testTag("health_tab_$tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBengali) tabsBn[idx] else tab,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Featured Hero Tip Card (with tea image)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("featured_tip_card"),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header image
                Image(
                    painter = painterResource(id = R.drawable.img_daily_tea_tip),
                    contentDescription = "Morning tea",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = AmberAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBengali) featuredTip.titleBn else "Today's Tip",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AmberAccent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = featuredTip.contentEn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = featuredTip.contentBn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { isFav = !isFav }) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFav) RoseAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "${featuredTip.contentEn}\n\n${featuredTip.contentBn}\n- VitalStep")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Tip"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = CyanAccent
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // More Categories Section (Matching screen 6)
        Text(
            text = if (isBengali) "আরো ক্যাটাগরি" else "More Categories",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryCircleItem(
                icon = Icons.Default.Restaurant,
                title = if (isBengali) "পুষ্টি" else "Nutrition",
                color = RoseAccent,
                onClick = { onNavigate(Screen.DIET_PLAN) },
                tag = "cat_nutrition"
            )
            CategoryCircleItem(
                icon = Icons.Default.DirectionsWalk,
                title = if (isBengali) "হাঁটা" else "Walking",
                color = CyanAccent,
                onClick = { onNavigate(Screen.EXERCISE_GUIDE) },
                tag = "cat_walking"
            )
            CategoryCircleItem(
                icon = Icons.Default.Bedtime,
                title = if (isBengali) "ঘুম" else "Sleep",
                color = PurpleAccent,
                onClick = {},
                tag = "cat_sleep"
            )
            CategoryCircleItem(
                icon = Icons.Default.SelfImprovement,
                title = if (isBengali) "মন" else "Mind",
                color = MintAccent,
                onClick = { onNavigate(Screen.DAILY_MOTIVATION) },
                tag = "cat_mind"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Other Wellness Tips List
        Text(
            text = if (isBengali) "অন্যান্য স্বাস্থ্য পরামর্শ" else "Daily Wellness Guides",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        tipsList.drop(1).forEach { tip ->
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isBengali) tip.titleBn else tip.titleEn,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isBengali) tip.contentBn else tip.contentEn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Health Disclaimer Card
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
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isBengali)
                        "গুরুত্বপূর্ণ ঘোষণা: এই অ্যাপে প্রদত্ত স্বাস্থ্য ও পুষ্টি পরামর্শ কেবল সাধারণ শিক্ষামূলক তথ্যের জন্য। রোগ নির্ণয় বা চিকিৎসার জন্য সর্বদা বিশেষজ্ঞ চিকিৎসকের পরামর্শ নিন।"
                    else
                        "Health Safety Notice: VitalStep provides educational wellness and general fitness information only. It is not intended for medical diagnosis, prescriptions, or replacing consultation with qualified healthcare professionals.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFFB0BEC5),
                        lineHeight = 16.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun CategoryCircleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    color: Color,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}
