package com.example.feedme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyInfo(
    @PrimaryKey val date: String,
    var steps: Long,
    var times_eaten: Int
)