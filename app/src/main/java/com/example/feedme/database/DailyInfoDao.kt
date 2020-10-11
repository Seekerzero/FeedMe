package com.example.feedme.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.feedme.DailyInfo

@Dao
interface DailyInfoDao {
    @Query("SELECT * from dailyInfo")
    fun getEntries(): LiveData<List<DailyInfo>>

    @Query("SELECT * FROM dailyInfo WHERE date=(:id)")
    fun getEntry(id: String): LiveData<DailyInfo?>

    @Update
    fun updateDailyInfo(dailyInfo: DailyInfo)

    @Insert
    fun addDailyInfo(dailyInfo: DailyInfo)

}