package com.example.feedme

import java.util.*

data class Award(
    val award_name: String,
    val award_date: Date?,
    val award_description_string_resource: Int,
    val award_icon: Int,
    val isEnabled: Boolean
)
