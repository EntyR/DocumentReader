package com.example.documentreader

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DocApplication(): Application() {
    override fun onCreate() {
        super.onCreate()
    }
}