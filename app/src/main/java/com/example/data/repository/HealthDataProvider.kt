package com.example.data.repository

import com.example.data.model.AchievementCategory
import com.example.data.model.AchievementItem
import com.example.data.model.ExerciseItem
import com.example.data.model.FoodItem
import com.example.data.model.HealthTip
import com.example.data.model.MotivationQuote

object HealthDataProvider {

    val achievementsList: List<AchievementItem> = listOf(
        // Steps Milestones
        AchievementItem(
            key = "step_1k",
            targetThreshold = 1000,
            category = AchievementCategory.STEPS,
            titleEn = "First Steps",
            titleBn = "প্রথম পদক্ষেপ",
            descEn = "Walked your first 1,000 steps today!",
            descBn = "আজ আপনার প্রথম ১,০০০ কদম পূর্ণ হয়েছে!",
            iconName = "footprint"
        ),
        AchievementItem(
            key = "step_2k",
            targetThreshold = 2000,
            category = AchievementCategory.STEPS,
            titleEn = "Little Mover",
            titleBn = "ছোট পদক্ষেপের শুরু",
            descEn = "Reached 2,000 steps! Movement builds momentum.",
            descBn = "২,০০০ কদম পূর্ণ! গতি সঞ্চার হচ্ছে।",
            iconName = "directions_walk"
        ),
        AchievementItem(
            key = "step_3k",
            targetThreshold = 3000,
            category = AchievementCategory.STEPS,
            titleEn = "Active Soul",
            titleBn = "সক্রিয় প্রাণ",
            descEn = "Achieved 3,000 steps! Halfway to a healthy daily minimum.",
            descBn = "৩,০০০ কদম অর্জিত! প্রতিদিনের সক্রিয়তার শুভ সূচনা।",
            iconName = "directions_run"
        ),
        AchievementItem(
            key = "step_5k",
            targetThreshold = 5000,
            category = AchievementCategory.STEPS,
            titleEn = "Daily Explorer",
            titleBn = "দৈনিক অনুসন্ধানকারী",
            descEn = "5,000 steps reached! You are officially in active territory.",
            descBn = "৫,০০০ কদম পূর্ণ! আপনি আনুষ্ঠানিকভাবে সক্রিয় সদস্য।",
            iconName = "explore"
        ),
        AchievementItem(
            key = "step_6k",
            targetThreshold = 6000,
            category = AchievementCategory.STEPS,
            titleEn = "Energy Booster",
            titleBn = "শক্তির নতুন সূচনা!",
            descEn = "6,000 steps reached! Heart rate and vitality upgraded.",
            descBn = "৬,০০০ কদম পূর্ণ! আপনার শক্তি ও উদ্দীপনা বৃদ্ধি পেয়েছে।",
            iconName = "bolt"
        ),
        AchievementItem(
            key = "step_8k",
            targetThreshold = 8000,
            category = AchievementCategory.STEPS,
            titleEn = "Fitness Rising",
            titleBn = "সুস্বাস্থ্যের উত্থান",
            descEn = "8,000 steps! Excellent cardiovascular boost.",
            descBn = "৮,০০০ কদম! হৃদযন্ত্রের কার্যক্ষমতা বৃদ্ধির চমৎকার অগ্রগতি।",
            iconName = "trending_up"
        ),
        AchievementItem(
            key = "step_10k",
            targetThreshold = 10000,
            category = AchievementCategory.STEPS,
            titleEn = "Step Champion",
            titleBn = "কদম চ্যাম্পিয়ন",
            descEn = "The golden 10,000 steps milestone conquered today!",
            descBn = "আজকের সোনালী ১০,০০০ কদমের লক্ষ্য সফলভাবে অর্জিত!",
            iconName = "military_tech"
        ),
        AchievementItem(
            key = "step_12k",
            targetThreshold = 12000,
            category = AchievementCategory.STEPS,
            titleEn = "Walking Hero",
            titleBn = "হাঁটার নায়ক",
            descEn = "Surpassed 12,000 steps with great stamina!",
            descBn = "১২,০০০ কদম অতিক্রম করে আপনি অসাধারণ সহনশীলতার পরিচয় দিয়েছেন!",
            iconName = "workspace_premium"
        ),
        AchievementItem(
            key = "step_15k",
            targetThreshold = 15000,
            category = AchievementCategory.STEPS,
            titleEn = "Endurance Master",
            titleBn = "ধৈর্য্যের মাস্টার",
            descEn = "15,000 steps! Outstanding physical endurance.",
            descBn = "১৫,০০০ কদম! অসাধারণ শারীরিক সহনশীলতা ও শক্তি।",
            iconName = "sports_score"
        ),
        AchievementItem(
            key = "step_20k",
            targetThreshold = 20000,
            category = AchievementCategory.STEPS,
            titleEn = "Elite Walker",
            titleBn = "অভিজাত পদচারী",
            descEn = "20,000 steps unlocked! An extraordinary accomplishment.",
            descBn = "২০,০০০ কদম আনলক হয়েছে! এটি একটি অসাধারণ সাফল্য।",
            iconName = "diamond"
        ),
        AchievementItem(
            key = "step_25k",
            targetThreshold = 25000,
            category = AchievementCategory.STEPS,
            titleEn = "Legendary Strider",
            titleBn = "কিংবদন্তি পদযাত্রী",
            descEn = "25,000 steps! Top 1% walking endurance worldwide.",
            descBn = "২৫,০০০ কদম! বিশ্বমানের শীর্ষ ১% পদচারীদের একজন।",
            iconName = "stars"
        ),
        AchievementItem(
            key = "step_30k",
            targetThreshold = 30000,
            category = AchievementCategory.STEPS,
            titleEn = "Ultimate Mover",
            titleBn = "চূড়ান্ত যোদ্ধা",
            descEn = "30,000 steps! Limitless energy and determination.",
            descBn = "৩০,০০০ কদম! সীমাহীন শক্তি ও অনমনীয় মানসিকতা।",
            iconName = "emoji_events"
        ),

        // Streaks
        AchievementItem(
            key = "streak_1d",
            targetThreshold = 1,
            category = AchievementCategory.STREAKS,
            titleEn = "First Day",
            titleBn = "প্রথম দিন",
            descEn = "Completed your very first logged walking day!",
            descBn = "আপনার অ্যাপে প্রথম হাঁটার দিন সম্পন্ন হয়েছে!",
            iconName = "looks_one"
        ),
        AchievementItem(
            key = "streak_3d",
            targetThreshold = 3,
            category = AchievementCategory.STREAKS,
            titleEn = "3-Day Streak",
            titleBn = "৩ দিনের ধারাবাহিকতা",
            descEn = "Consistency unlocked: 3 consecutive active days.",
            descBn = "টানা ৩ দিন হাঁটার অভ্যাস বজায় রাখা হয়েছে।",
            iconName = "local_fire_department"
        ),
        AchievementItem(
            key = "streak_7d",
            targetThreshold = 7,
            category = AchievementCategory.STREAKS,
            titleEn = "7-Day Streak",
            titleBn = "৭ দিনের চ্যাম্পিয়ন",
            descEn = "A full week of non-stop daily walking power!",
            descBn = "একটি পূর্ণ সপ্তাহ প্রতিদিন নিয়মিত হাঁটার মাইলফলক!",
            iconName = "whatshot"
        ),
        AchievementItem(
            key = "streak_30d",
            targetThreshold = 30,
            category = AchievementCategory.STREAKS,
            titleEn = "30-Day Master",
            titleBn = "৩০ দিনের মাস্টার",
            descEn = "One full month of healthy lifestyle habit formed.",
            descBn = "এক মাস জুড়ে প্রতিদিন সক্রিয় থাকার অভ্যাস গড়ে উঠেছে।",
            iconName = "hotel_class"
        ),

        // Special
        AchievementItem(
            key = "water_hero",
            targetThreshold = 2500,
            category = AchievementCategory.SPECIAL,
            titleEn = "Hydration Master",
            titleBn = "জলপান সচেতন",
            descEn = "Logged full daily water target of 2,500 ml!",
            descBn = "দৈনিক ২,৫০০ মিলি পানি পানের লক্ষ্য পূরণ হয়েছে!",
            iconName = "water_drop"
        ),
        AchievementItem(
            key = "early_bird",
            targetThreshold = 3000,
            category = AchievementCategory.SPECIAL,
            titleEn = "Morning Champion",
            titleBn = "ভোরের চ্যাম্পিয়ন",
            descEn = "Completed over 3,000 steps before 10:00 AM.",
            descBn = "সকাল ১০টার আগেই ৩,০০০ কদম সম্পন্ন করেছেন।",
            iconName = "wb_sunny"
        )
    )

    val healthTips: List<HealthTip> = listOf(
        HealthTip(
            id = 1,
            titleEn = "Start With Warm Water",
            titleBn = "আজকের পরামর্শ",
            contentEn = "Start your day with a glass of warm water to boost metabolism, stimulate digestion, and rehydrate after night sleep.",
            contentBn = "সকালে গরম পানি পান করলে শরীরের বিপাক ক্রিয়া (metabolism) বৃদ্ধি পায় এবং হজম প্রক্রিয়া সুন্দর হয়।",
            category = "Hydration",
            timeOfDay = "Morning",
            imageResName = "img_daily_tea_tip"
        ),
        HealthTip(
            id = 2,
            titleEn = "Post-Meal Gentle Walk",
            titleBn = "খাবারের পরে মৃদু হাঁটা",
            contentEn = "A gentle 10 to 15-minute walk after lunch or dinner regulates blood glucose spikes and enhances digestive efficiency.",
            contentBn = "দুপুরের বা রাতের খাবারের পর ১০-১৫ মিনিট ধীরে ধীরে হাঁটলে রক্তে শর্করার মাত্রা নিয়ন্ত্রণে থাকে এবং খাবার সহজে হজম হয়।",
            category = "Walking",
            timeOfDay = "Afternoon"
        ),
        HealthTip(
            id = 3,
            titleEn = "Brisk Walking for Heart Vitality",
            titleBn = "হৃদযন্ত্রের যত্নে দ্রুত হাঁটা",
            contentEn = "Brisk walking at 100-120 steps per minute strengthens heart muscles, lowers bad LDL cholesterol, and releases endorphins.",
            contentBn = "প্রতি মিনিটে ১০০-১২০ কদম গতিতে দ্রুত হাঁটলে হৃৎপিণ্ড সবল হয়, ক্ষতিকর কোলেস্টেরল হ্রাস পায় এবং মানসিক প্রফুল্লতা বাড়ে।",
            category = "Heart",
            timeOfDay = "Morning"
        ),
        HealthTip(
            id = 4,
            titleEn = "Mindful Hydration Habits",
            titleBn = "সঠিক জলপান অভ্যাস",
            contentEn = "Drink water in small sips throughout the day rather than chugging large amounts at once. Keep your hydration constant.",
            contentBn = "একবারে অনেক বেশি পানি পান না করে সারা দিনে ছোট ছোট চুমুকে পানি পান করুন। এতে কোষগুলো ভালোভাবে হাইড্রেট হয়।",
            category = "Hydration",
            timeOfDay = "Afternoon"
        ),
        HealthTip(
            id = 5,
            titleEn = "Evening Wind-Down & Blue Light",
            titleBn = "সন্ধ্যার মানসিক প্রশান্তি",
            contentEn = "Dim screens and artificial lighting 1 hour before sleep to support natural melatonin production and deep restorative sleep.",
            contentBn = "ঘুমানোর অন্তত ১ ঘণ্টা আগে মোবাইল বা টিভি স্ক্রিন বন্ধ রাখুন, যাতে মেলাটোনিন হরমোন নিঃসরণ হয়ে গভীর ঘুম হতে পারে।",
            category = "Sleep",
            timeOfDay = "Night"
        ),
        HealthTip(
            id = 6,
            titleEn = "Mindful Breathing During Walks",
            titleBn = "হাঁটার সময় সচেতন শ্বাস-প্রশ্বাস",
            contentEn = "Practice 4-step inhalation and 4-step exhalation while walking in fresh air. It calms the nervous system.",
            contentBn = "খোলা বাতাসে হাঁটার সময় ৪ কদমে শ্বাস গ্রহণ এবং পরবর্তী ৪ কদমে শ্বাস ত্যাগের ছন্দ বজায় রাখুন। এটি স্নায়ুকে শান্ত রাখে।",
            category = "Mind",
            timeOfDay = "Evening"
        )
    )

    val foodLibrary: List<FoodItem> = listOf(
        FoodItem(
            id = 1,
            nameEn = "Healthy Oats & Milk Bowl",
            nameBn = "স্বাস্থ্যকর ওটস ও দুধ",
            category = "Breakfast",
            calories = 320,
            proteinG = 12f,
            carbsG = 48f,
            fatG = 7f,
            servingEn = "1 bowl (40g oats + 200ml milk)",
            servingBn = "১ বাটি (৪০ গ্রাম ওটস + ২০০ মিলি দুধ)",
            tipsEn = listOf(
                "Oats with fresh sliced fruits",
                "1 small banana for natural potassium",
                "5-6 raw almonds",
                "1 glass of low fat warm milk"
            ),
            tipsBn = listOf(
                "ওটস সঙ্গে তাজা ফল",
                "১টি ছোট কলা (প্রাকৃতিক পটাশিয়াম)",
                "৫-৬টি কাঠবাদাম",
                "১ গ্লাস হালকা গরম দুধ"
            ),
            imageResName = "img_healthy_breakfast"
        ),
        FoodItem(
            id = 2,
            nameEn = "Brown Rice, Dal & Fish",
            nameBn = "লাল চালের ভাত, ডাল ও রুই মাছ",
            category = "Lunch",
            calories = 460,
            proteinG = 28f,
            carbsG = 62f,
            fatG = 9f,
            servingEn = "1 cup cooked rice, 1 bowl dal, 1 grilled fish fillet",
            servingBn = "১ কাপ ভাত, ১ বাটি ঘন ডাল, ১ টুকরো মাছ",
            tipsEn = listOf(
                "Rich in lean omega-3 protein",
                "Yellow lentil dal gives soluble fiber",
                "Add cucumber & tomato fresh salad",
                "Use minimal mustard oil in preparation"
            ),
            tipsBn = listOf(
                "ওমেগা-৩ ও উচ্চমানের আমিষ",
                "মুগ বা মসুর ডাল থেকে পর্যাপ্ত ফাইবার",
                "শসা ও টমেটোর লেবুর সালাদ যুক্ত করুন",
                "রান্নায় পরিমিত তেল ব্যবহার করুন"
            )
        ),
        FoodItem(
            id = 3,
            nameEn = "Whole Wheat Roti & Mixed Sabji",
            nameBn = "আটার রুটি ও রঙিন সবজি",
            category = "Dinner",
            calories = 290,
            proteinG = 9f,
            carbsG = 52f,
            fatG = 4.5f,
            servingEn = "2 medium handmade rotis + 1 bowl sabji",
            servingBn = "২টি আটার রুটি + ১ বাটি মিক্সড সবজি",
            tipsEn = listOf(
                "Light evening meal for smooth sleep",
                "Spinach, papaya, and beans for micronutrients",
                "Keep dinner 2.5 hours prior to bed"
            ),
            tipsBn = listOf(
                "রাতের সহজপাচ্য পুষ্টিকর খাবার",
                "পালং শাক, পেঁপে, গাজর ও বরবটির সবজি",
                "ঘুমানোর আড়াই ঘণ্টা আগে রাতের খাবার শেষ করুন"
            )
        ),
        FoodItem(
            id = 4,
            nameEn = "Boiled Eggs & Seasonal Fruit",
            nameBn = "সিদ্ধ ডিম ও পেয়ারা/আপেল",
            category = "Snacks",
            calories = 160,
            proteinG = 13f,
            carbsG = 14f,
            fatG = 5f,
            servingEn = "1 or 2 boiled eggs + 1 seasonal fruit",
            servingBn = "১-২টি সিদ্ধ ডিম + ১টি তাজা পেয়ারা বা আপেল",
            tipsEn = listOf(
                "Complete amino acid profile",
                "High Vitamin C from fresh guava",
                "Avoid fried snacks"
            ),
            tipsBn = listOf(
                "শরীরের জন্য পূর্ণাঙ্গ প্রোটিন ও পুষ্টি",
                "তাজা দেশি পেয়ারা থেকে প্রচুর ভিটামিন সি",
                "তেলে ভাজা স্ন্যাক্স পরিহার করুন"
            )
        )
    )

    val exerciseList: List<ExerciseItem> = listOf(
        ExerciseItem(
            id = 1,
            titleEn = "Brisk Walking",
            titleBn = "দ্রুত হাঁটা (Brisk Walking)",
            category = "Walking",
            durationMin = 25,
            difficulty = "Medium",
            caloriesEstimate = 120,
            summaryEn = "A rhythmic, moderate-to-vigorous cardio walk to train cardiovascular capacity and boost mental focus.",
            summaryBn = "হৃদযন্ত্রের স্বাস্থ্য ভালো রাখতে ও প্রতিদিনের ক্লান্তি দূর করতে দ্রুত হাঁটা অত্যন্ত কার্যকরী!",
            stepsListEn = listOf(
                "Keep your posture tall, shoulders relaxed and eyes looking 10-15 feet ahead.",
                "Swing arms naturally bent at approximately 90 degrees.",
                "Strike the ground lightly with the heel and roll smoothly onto toes.",
                "Maintain a pace where speaking is possible, but singing is difficult."
            ),
            stepsListBn = listOf(
                "মেরুদণ্ড সোজা রাখুন, কাঁধ শিথিল রাখুন এবং দৃষ্টি সামনের দিকে রাখুন।",
                "কনুই ৯০ ডিগ্রিতে বাঁকিয়ে হাতের স্বাভাবিক ছন্দ বজায় রাখুন।",
                "পায়ের গোড়ালি দিয়ে মাটিতে স্পর্শ করে আলতোভাবে আঙুলের দিকে চাপ দিন।",
                "এমন গতিতে হাঁটুন যাতে কথা বলা যায় কিন্তু গান গাওয়া কষ্টকর হয়।"
            ),
            safetyTipsEn = "Wear supportive cushioned shoes and stay hydrated in hot humidity.",
            safetyTipsBn = "নরম ও আরামদায়ক জুতো পরুন এবং অতিরিক্ত গরমে পর্যাপ্ত পানি পান করুন।",
            imageResName = "img_brisk_walking"
        ),
        ExerciseItem(
            id = 2,
            titleEn = "Morning Ankle & Calf Stretches",
            titleBn = "সকালের গোড়ালি ও পেশির স্ট্রেচিং",
            category = "Stretching",
            durationMin = 8,
            difficulty = "Easy",
            caloriesEstimate = 35,
            summaryEn = "Gentle pre-walk stretches to warm up the Achilles tendon, calves, and plantar fascia.",
            summaryBn = "হাঁটা শুরুর আগে পায়ের পেশি সচল রাখতে সহজ ও নিরাপদ স্ট্রেচিং।",
            stepsListEn = listOf(
                "Stand an arm's length from a wall with hands on the wall.",
                "Step one foot back, keep it flat on the ground, and bend the front knee.",
                "Hold for 20 seconds, feeling a gentle stretch in the calf.",
                "Switch legs and repeat 3 times."
            ),
            stepsListBn = listOf(
                "দেয়াল থেকে এক হাত দূরত্বে সোজা হয়ে দাঁড়ান।",
                "এক পা পেছনে নিন, গোড়ালি মাটিতে রাখুন এবং সামনের হাঁটু সামান্য বাঁকান।",
                "২০ সেকেন্ড ধরে রাখুন, যেন পায়ের পেছনের পেশিতে মৃদু টান অনুভূত হয়।",
                "পা পরিবর্তন করে ৩ বার পুনরাবৃত্তি করুন।"
            ),
            safetyTipsEn = "Never bounce while stretching. Hold gently without pain.",
            safetyTipsBn = "স্ট্রেচিংয়ের সময় ঝাঁকুনি দেবেন না। ব্যথাহীন ও স্বাভাবিকভাবে ধরে রাখুন।"
        ),
        ExerciseItem(
            id = 3,
            titleEn = "Gentle Cool-Down Stroll",
            titleBn = "হাঁটা শেষে শরীর শিথিলকরণ",
            category = "Beginner",
            durationMin = 10,
            difficulty = "Easy",
            caloriesEstimate = 40,
            summaryEn = "Lowering the heart rate gradually back to normal resting levels after intense exercise.",
            summaryBn = "দ্রুত হাঁটার পর ধীরে ধীরে হৃদস্পন্দন স্বাভাবিক অবস্থায় ফিরিয়ে আনা।",
            stepsListEn = listOf(
                "Slow down walking pace by 50% for 5 minutes.",
                "Deep diaphragmatic breaths: inhale for 4 seconds, exhale for 6 seconds.",
                "Shoulder rolls forward and backwards 10 times."
            ),
            stepsListBn = listOf(
                "৫ মিনিটের জন্য হাঁটার গতি অর্ধেক কমিয়ে দিন।",
                "গভীর শ্বাস নিন: ৪ সেকেন্ডে পেট ভরে শ্বাস নিন, ৬ সেকেন্ডে ধীরে ধীরে ছাড়ুন।",
                "কাঁধ বৃত্তাকারে সামনে ও পেছনে ১০ বার ঘোরান।"
            ),
            safetyTipsEn = "Do not sit down immediately after high-intensity fast walking.",
            safetyTipsBn = "দ্রুত হাঁটার পরপরই হুট করে বসে পড়বেন না।"
        )
    )

    val motivationQuotes: List<MotivationQuote> = listOf(
        MotivationQuote(
            id = 1,
            quoteEn = "Small steps today, a healthier tomorrow.",
            quoteBn = "আজ একটু হাঁটেন, আগামীকাল আরো সুস্থ থাকুন!",
            author = "VitalStep Wellness"
        ),
        MotivationQuote(
            id = 2,
            quoteEn = "A healthier you is a happier you.",
            quoteBn = "একটি আজকের চেষ্টা, একটি সুন্দর আগামীকাল!!",
            author = "VitalStep Wisdom"
        ),
        MotivationQuote(
            id = 3,
            quoteEn = "Consistency is the mother of mastery and longevity.",
            quoteBn = "প্রতিদিনের নিয়মিত অভ্যাসের মধ্যেই দীর্ঘ ও সুস্থ জীবনের শক্তি নিহিত।",
            author = "VitalStep Daily"
        ),
        MotivationQuote(
            id = 4,
            quoteEn = "Walking is man's best medicine.",
            quoteBn = "নিয়মিত হাঁটা হলো সুস্বাস্থ্যের সবচেয়ে সেরা প্রাকৃতিক মহৌষধ।",
            author = "Hippocrates"
        ),
        MotivationQuote(
            id = 5,
            quoteEn = "Every single step counts towards the person you want to become.",
            quoteBn = "আপনার প্রতিটি কদম আপনাকে এগিয়ে নিয়ে যায় সুস্থ ও সুন্দর জীবনের দিকে।",
            author = "VitalStep Inspiration"
        )
    )
}
