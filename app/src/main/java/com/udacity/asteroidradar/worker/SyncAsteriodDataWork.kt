package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDataBase
import com.udacity.asteroidradar.repository.AsteroidRepository
import java.net.SocketTimeoutException

/**
 * The Worker class will Sync the Network Asteroid Data from the Nasa Server.
 * The Sync will happen based on the constaints.
 */
class SyncAsteriodDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val database = getDataBase(appContext)

    private val repository = AsteroidRepository(database)

    override suspend fun doWork(): Result {
        return try {
            repository.syncPictureOfTheDay()
            repository.syncNetworkAsteroid()
            repository.deleteOldAsteroidFromDatabase()
            Result.success()
        } catch (e: SocketTimeoutException) {
            Result.retry()
        }
    }


    companion object {
        const val WORK_NAME = "AsteriodDataWorker"
    }

}