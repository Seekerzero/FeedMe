package com.example.feedme

import android.app.Application

class FeedMeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DailyInfoRepository.initialize(this)
    }
}