package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.liquidGlass
import com.example.ui.viewmodel.Screen

data class NavItem(
    val screen: Screen,
    val titleEn: String,
    val titleBn: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@Composable
fun FloatingGlassBottomNav(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    isBengali: Boolean,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(Screen.HOME, "Home", "হোম", Icons.Filled.Home, Icons.Outlined.Home, "nav_tab_home"),
        NavItem(Screen.ACTIVITY, "Activity", "অ্যাক্টিভিটি", Icons.Filled.Timeline, Icons.Outlined.Timeline, "nav_tab_activity"),
        NavItem(Screen.HEALTH, "Health", "স্বাস্থ্য", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "nav_tab_health"),
        NavItem(Screen.PROFILE, "Profile", "প্রোফাইল", Icons.Filled.Person, Icons.Outlined.PersonOutline, "nav_tab_profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .liquidGlass(
                    shape = RoundedCornerShape(32.dp),
                    elevation = 8.dp
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.screen
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) CyanAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(250),
                    label = "nav_icon_color"
                )

                Column(
                    modifier = Modifier
                        .testTag(item.tag)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, radius = 28.dp),
                            onClick = { onNavigate(item.screen) }
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .then(
                                if (isSelected) Modifier
                                    .clip(CircleShape)
                                    .background(Color(0x1F00E5FF))
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = if (isBengali) item.titleBn else item.titleEn,
                            tint = iconColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Text(
                        text = if (isBengali) item.titleBn else item.titleEn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = iconColor
                        )
                    )
                }
            }
        }
    }
}
