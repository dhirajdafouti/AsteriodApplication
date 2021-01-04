package com.udacity.asteroidradar

import android.app.Application
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.worker.SyncAsteriodDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 */
class AsteroidApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initAsteroidDataWorker()
    }

    private fun initAsteroidDataWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.METERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        applicationScope.launch {
            val repeatingRequest = PeriodicWorkRequestBuilder<SyncAsteriodDataWork>(
                repeatInterval = 1, repeatIntervalTimeUnit = TimeUnit.DAYS
            ).setConstraints(constraints)
                .build()
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                SyncAsteriodDataWork.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, repeatingRequest
            )
        }
    }

    companion object {
        private val TAG: String = AsteroidApplication::class.java.simpleName
    }
}




