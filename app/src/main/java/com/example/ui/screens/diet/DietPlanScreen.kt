package com.example.ui.screens.diet

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
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FoodItem
import com.example.ui.components.LiquidGlassCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MintAccent

@Composable
fun DietPlanScreen(
    foodItems: List<FoodItem>,
    isBengali: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedCategory by remember { mutableStateOf("Breakfast") }
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf("Breakfast", "Lunch", "Dinner", "Snacks")
    val categoriesBn = listOf("সকালের নাস্তা", "দুপুরের খাবার", "রাতের খাবার", "হালকা নাস্তা")

    val filteredList = foodItems.filter {
        (searchQuery.isEmpty() || it.nameEn.contains(searchQuery, ignoreCase = true) || it.nameBn.contains(searchQuery)) &&
                it.category == selectedCategory
    }

    val featuredMeal = foodItems.find { it.category == selectedCategory } ?: foodItems.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("diet_plan_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("diet_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBengali) "খাদ্য তালিকা ও পুষ্টি" else "Diet Plan",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Meal Tabs (Breakfast, Lunch, Dinner, Snacks)
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
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) CyanAccent else Color.Transparent)
                        .clickable { selectedCategory = cat }
                        .testTag("diet_tab_$cat"),
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

        Spacer(modifier = Modifier.height(18.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (isBengali) "খাবার বা পুষ্টি খুঁজুন..." else "Search healthy food items...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = CyanAccent
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyanAccent,
                unfocusedBorderColor = Color(0x3300E5FF)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("food_search_input")
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Featured Meal Card (Matches mockup screen 7)
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("featured_meal_card"),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Food Image
                Image(
                    painter = painterResource(id = R.drawable.img_healthy_breakfast),
                    contentDescription = featuredMeal.nameEn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBengali) featuredMeal.nameBn else featuredMeal.nameEn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = if (isBengali) featuredMeal.servingBn else featuredMeal.servingEn,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x33FFB300))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${featuredMeal.calories} kcal",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AmberAccent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Macronutrients Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MacroChip(label = if (isBengali) "প্রোটিন" else "Protein", value = "${featuredMeal.proteinG}g", color = CyanAccent)
                    MacroChip(label = if (isBengali) "কার্বোহাইড্রেট" else "Carbs", value = "${featuredMeal.carbsG}g", color = MintAccent)
                    MacroChip(label = if (isBengali) "ফ্যাট" else "Fat", value = "${featuredMeal.fatG}g", color = AmberAccent)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bullet Items
                Text(
                    text = if (isBengali) "উপাদান ও পরিবেশন:" else "Suggested Ingredients & Tips:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                val tips = if (isBengali) featuredMeal.tipsBn else featuredMeal.tipsEn
                tips.forEach { tip ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MintAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tip,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // General Nutrition Advice
        Text(
            text = if (isBengali) "সুস্থ থাকার খাদ্য নির্দেশিকা" else "Healthy Eating Guidelines",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isBengali)
                        "১. খাদ্যতালিকায় দেশি মৌসুমি ফল ও রঙিন শাকসবজি রাখুন।\n২. প্রক্রিয়াজাত খাবার ও অতিরিক্ত চিনিযুক্ত কোমল পানীয় পরিহার করুন।\n৩. পর্যাপ্ত পানি পান করুন ও খাবারের মাঝে হালকা হাঁটার অভ্যাস বজায় রাখুন।"
                    else
                        "1. Prioritize seasonal local fruits and rich vibrant vegetables.\n2. Limit ultra-processed foods and artificial sugary drinks.\n3. Stay hydrated consistently and maintain a 15-minute gentle walk after heavy meals.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 20.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun MacroChip(
    label: String,
    value: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
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
