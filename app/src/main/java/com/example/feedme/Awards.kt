package com.example.feedme

/**
 * Enum for all 9 awards to use in AwardListViewModel
 */
enum class Awards(val awardName: String, val description: Int, val icon: Int) {
    HEALTH_1(
        "Happy Chomper",
        R.string.health_1_desc,
        R.drawable.health_1
    ),
    HEALTH_7(
        "Healthy Chomper",
        R.string.health_7_desc,
        R.drawable.health_7
    ),
    HEALTH_30(
        "Strong & Healthy Chomper",
        R.string.health_30_desc,
        R.drawable.health_30
    ),
    WALK_1(
        "Healthy Heart",
        R.string.award_healthy_heart_desc,
        R.drawable.walk_2000_1day
    ),
    WALK_2(
        "Health Fighter",
        R.string.award_healthy_heart_2_desc,
        R.drawable.walk_2000_7days
    ),
    WALK_3(
        "Health Warrior",
        R.string.award_healthy_heart_5000_1_desc,
        R.drawable.walk_5000_1_day
    ),
    TRAVELLER(
        "World Travller",
        R.string.travelling_desc,
        R.drawable.japan_medal
    ),
    HAPPY_MOCHI(
        "Extreme Health",
        R.string.extreme_health_desc,
        R.drawable.happy_mochi_medal
    ),
    AGE(
        "They Grow Up So Fast",
        R.string.age_desc,
        R.drawable.age_100_days
    )
}