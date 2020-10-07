package com.example.feedme

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.random.Random

private const val TAG = "DailyInfoListViewModel"

class DailyInfoListViewModel : ViewModel() {
    private val dailyInfoRepository = DailyInfoRepository.get()
    val dailyInfoLiveData = dailyInfoRepository.getEntries()

    // initialize with dummy data
    fun initializeWithDummyData() {
        Log.d(TAG, "Loading dummy data")
        for (i in 0 until 10) {
            addDailyInfo(
                DailyInfo(
                    Date((Random.nextInt()).toLong()),
                    Random.nextLong(0, 100),
                    Random.nextInt(0, 100)
                )
            )
        }
    }

    /**
     * Add a new game to the database
     */
    fun addDailyInfo(dailyInfo: DailyInfo) {
        dailyInfoRepository.addDailyInfo(dailyInfo)
    }

    /**
     * Update an existing game in the database
     */
    fun updateDailyInfo(dailyInfo: DailyInfo) {
        dailyInfoRepository.updateDailyInfo(dailyInfo)
    }

}