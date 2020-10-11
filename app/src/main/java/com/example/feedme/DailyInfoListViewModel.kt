package com.example.feedme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

private const val TAG = "DailyInfoListViewModel"

class DailyInfoListViewModel : ViewModel() {
    private val dailyInfoRepository = DailyInfoRepository.get()
    val dailyInfoLiveData = dailyInfoRepository.getEntries()

    // initialize with dummy data
    fun initializeWithDummyData() {
        Log.d(TAG, "Loading dummy data.")
        for (i in 0 until 10) {
            addDailyInfo(
                DailyInfo(
                    nextInt().toString(),
                    Random.nextLong(0, 100),
                    nextInt(0, 100)
                )
            )
        }
    }

    /**
     * Add a new entry to the database
     */
    fun addDailyInfo(dailyInfo: DailyInfo) {
        dailyInfoRepository.addDailyInfo(dailyInfo)
    }

    /**
     * Update an existing entry in the database
     */
    fun updateDailyInfo(dailyInfo: DailyInfo) {
        dailyInfoRepository.updateDailyInfo(dailyInfo)
    }

    fun getEntry(id: String): LiveData<DailyInfo?> {
        return dailyInfoRepository.getEntry(id)
    }

}