package com.example.feedme

import java.util.*

/**
 * For use in the Awards List View Model
 */
data class Award(
    val award_name: String,
    val award_date: Date?,
    val award_description_string_resource: Int,
    val award_icon: Int,
    val isEnabled: Boolean
)
