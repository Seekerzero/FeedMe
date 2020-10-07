package com.example.feedme.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.feedme.DailyInfo

@Database(entities = [DailyInfo::class], version = 1)
@TypeConverters(DailyInfoTypeConverters::class)
abstract class DailyInfoDatabase : RoomDatabase() {
    abstract fun dailyInfoDao(): DailyInfoDao
}