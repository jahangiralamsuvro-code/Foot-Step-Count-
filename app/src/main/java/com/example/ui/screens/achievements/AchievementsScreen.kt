package com.example.ui.screens.achievements

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AchievementCategory
import com.example.data.model.AchievementItem
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent

@Composable
fun AchievementsScreen(
    achievements: List<AchievementItem>,
    isBengali: Boolean,
    onSelectAchievement: (AchievementItem) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(AchievementCategory.STEPS) }

    val filteredList = achievements.filter { it.category == selectedCategory }
    val featuredAchievement = achievements.find { it.key == "step_6k" } ?: achievements.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("achievements_screen")
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
                    .background(Color(0x33FFB300)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = AmberAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (isBengali) "অর্জন ও পদক গ্যালারি" else "Achievements",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Tabs (Steps, Streaks, Special)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x1F00E5FF))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val categories = listOf(
                AchievementCategory.STEPS to (if (isBengali) "কদম" else "Steps"),
                AchievementCategory.STREAKS to (if (isBengali) "ধারাবাহিকতা" else "Streaks"),
                AchievementCategory.SPECIAL to (if (isBengali) "বিশেষ" else "Special")
            )

            categories.forEach { (cat, title) ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) CyanAccent else Color.Transparent)
                        .clickable { selectedCategory = cat }
                        .testTag("achieve_tab_${cat.name}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF00363A) else MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Featured Medal Card (Matches design screen 5)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("featured_medal_card"),
            shape = RoundedCornerShape(26.dp),
            backgroundColor = Color(0xCC17233B),
            borderColor = AmberAccent.copy(alpha = 0.5f),
            onClick = { onSelectAchievement(featuredAchievement) }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Golden Medallion Badge
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFFFFD54F), Color(0xFFFF8F00))
                            )
                        )
                        .border(3.dp, AmberAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = "Medal",
                        tint = Color(0xFF3E2723),
                        modifier = Modifier.size(52.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "6,000",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                )

                Text(
                    text = if (isBengali) "Energy Booster • শক্তির নতুন সূচনা!" else "Energy Booster",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                )

                Text(
                    text = if (isBengali) "আপনি ৬,০০০ কদম পূর্ণ করেছেন!" else "You've reached 6,000 steps!",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFB0BEC5)
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Grid of Badges
        Text(
            text = if (isBengali) "সকল পদক তালিকা" else "All Badges",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .testTag("badges_grid")
        ) {
            items(filteredList) { item ->
                BadgeItemView(
                    item = item,
                    isBengali = isBengali,
                    onClick = { onSelectAchievement(item) }
                )
            }
        }
    }
}

@Composable
private fun BadgeItemView(
    item: AchievementItem,
    isBengali: Boolean,
    onClick: () -> Unit
) {
    LiquidGlassCard(
        modifier = Modifier
            .height(115.dp)
            .clickable(onClick = onClick)
            .testTag("badge_${item.key}"),
        shape = RoundedCornerShape(18.dp),
        borderWidth = if (item.isUnlocked) 1.2.dp else 0.8.dp,
        borderColor = if (item.isUnlocked) CyanAccent.copy(alpha = 0.6f) else Color(0x1FFFFFFF)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (item.isUnlocked) Color(0x3300E5FF) else Color(0x1A9E9E9E)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (item.isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = "Unlocked",
                        tint = CyanAccent,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFF78909C),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.progressLabel,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (item.isUnlocked) CyanAccent else Color(0xFF90A4AE),
                    fontSize = 11.sp
                )
            )

            Text(
                text = if (isBengali) item.titleBn else item.titleEn,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}
