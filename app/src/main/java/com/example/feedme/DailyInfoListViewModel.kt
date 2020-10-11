package com.example.feedme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "DailyInfoListViewModel"

class DailyInfoListViewModel : ViewModel() {
    private val dailyInfoRepository = DailyInfoRepository.get()
    val dailyInfoLiveData = dailyInfoRepository.getEntries()

    // initialize with dummy data
    fun initializeWithDummyData() {
        Log.d(TAG, "Loading dummy data. This part is commented out for now.")
//        for (i in 0 until 10) {
//            addDailyInfo(
//                DailyInfo(
//                    Date(100),//Date((nextInt()).toLong()),
//                    Random.nextLong(0, 100),
//                    nextInt(0, 100)
//                )
//            )
//        }
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

    fun getEntries(): LiveData<List<DailyInfo>> {
        return dailyInfoRepository.getEntries()
    }

    fun getEntry(id: Date): LiveData<DailyInfo?> {
        return dailyInfoRepository.getEntry(id)
    }

}