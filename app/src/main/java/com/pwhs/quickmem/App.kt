package com.pwhs.quickmem

import android.app.Application
import android.os.StrictMode
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Timber initialization
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            val policy = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build()
            StrictMode.setThreadPolicy(policy)
        }
        Purchases.logLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.ERROR
        Purchases.configure(
            PurchasesConfiguration.Builder(
                context = this,
                apiKey = BuildConfig.REVENUECAT_API_KEY,
            ).build()
        )
    }
}