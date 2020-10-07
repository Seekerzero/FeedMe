package com.example.feedme

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class DailyInfo(
    @PrimaryKey val date: Date,
    var steps: Long,
    var times_eaten: Int
)