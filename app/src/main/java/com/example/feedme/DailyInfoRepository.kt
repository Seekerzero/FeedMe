package com.example.feedme

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.feedme.database.DailyInfoDatabase
import java.util.*
import java.util.concurrent.Executors


private const val DATABASE_NAME = "dailyInfo-database"

class DailyInfoRepository private constructor(context: Context) {
    private val filesDir = context.applicationContext.filesDir
    private val database: DailyInfoDatabase = Room.databaseBuilder(
        context.applicationContext,
        DailyInfoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val dailyInfoDao = database.dailyInfoDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getEntries(): LiveData<List<DailyInfo>> = dailyInfoDao.getEntries()
    fun getEntry(id: Date): LiveData<DailyInfo?> = dailyInfoDao.getEntry(id)

    fun updateDailyInfo(dailyInfo: DailyInfo) {
        executor.execute {
            dailyInfoDao.updateDailyInfo(dailyInfo)
        }
    }

    fun addDailyInfo(dailyInfo: DailyInfo) {
        executor.execute {
            dailyInfoDao.addDailyInfo(dailyInfo)
        }
    }

    companion object {
        private var INSTANCE: DailyInfoRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DailyInfoRepository(context)
            }
        }

        fun get(): DailyInfoRepository {
            return INSTANCE
                ?: throw IllegalStateException("DailyInfoRepository must be initialized")
        }
    }
}
