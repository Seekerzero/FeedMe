package com.example.feedme

enum class Awards(val awardName: String, val description: Int, val icon: Int) {
    HEALTHY_HEART(
        "Healthy Heart Award",
        R.string.award_healthy_heart_description,
        R.drawable.pineapple
    ),
    STRONG_EATER(
        "Strong Eater Award",
        R.string.award_strong_eater_description,
        R.drawable.strawberry
    )
}