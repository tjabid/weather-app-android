package com.tj.weather

import android.app.Application
import com.tj.weather.di.AppContainer

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}
