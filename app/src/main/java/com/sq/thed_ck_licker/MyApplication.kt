package com.sq.thed_ck_licker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Perform any app-wide initialization here, if needed.
        // For example:
        // - Initialize logging frameworks
        // - Set up analytics
        // - Load configurations, etc.
    }
}