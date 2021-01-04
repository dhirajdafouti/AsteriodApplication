package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.constant.Constants
import com.udacity.asteroidradar.constant.DataBaseFetchType
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidPictureOfDayDatabase
import com.udacity.asteroidradar.database.AsteroidRoomDataBase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.networkModel.NetworkAsteroidsContainer
import com.udacity.asteroidradar.network.networkModel.asDatabaseModel
import com.udacity.asteroidradar.network.service.AsteroidApiService
import com.udacity.asteroidradar.util.getEndDate
import com.udacity.asteroidradar.util.getStartDate
import com.udacity.asteroidradar.util.parseAsteroidsJsonResult
import com.udacity.asteroidradar.util.parsePictureOfTheDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val database: AsteroidRoomDataBase) {

    private val _asteroidFetchType = MutableLiveData(DataBaseFetchType.ALL)
    private val asteroidFetchType: LiveData<DataBaseFetchType>
        get() = _asteroidFetchType

    @RequiresApi(Build.VERSION_CODES.O)
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.switchMap(asteroidFetchType) { type ->
            when (type) {
                DataBaseFetchType.ALL ->
                    Transformations.map(database.asteroidDao.getAllAsteroids()) {
                        it.asDomainModel()
                    }

                DataBaseFetchType.WEEKLY ->
                    Transformations.map(
                        database.asteroidDao.getWeeklyAsteroids(
                            getStartDate(),
                            getEndDate()
                        )
                    ) { it.asDomainModel() }

                DataBaseFetchType.TODAY ->
                    Transformations.map(database.asteroidDao.getTodayAsteroids(getStartDate())) {
                        it.asDomainModel()
                    }
                else -> throw IllegalArgumentException(" Invalid DataBase Fetch type !")
            }

        }

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(database.pictureDao.getAsteroidPictureOfDay()) {
            it?.asDomainModel()
        }

    suspend fun syncNetworkAsteroid() {
        withContext(Dispatchers.IO) {
            val jsonAsteroidResult = AsteroidApiService.retrofitService.getNetworkAsteroid()
            val jsonAsteroidResultParsed = parseAsteroidsJsonResult(JSONObject(jsonAsteroidResult))
            database.asteroidDao.insertAllAsteroidData(
                *NetworkAsteroidsContainer(
                    jsonAsteroidResultParsed
                ).asDatabaseModel()
            )
        }
    }


    suspend fun syncPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            val jsonPictureOfDayResult =
                AsteroidApiService.retrofitService.getNetworkPictureOfTheDay(getStartDate())
            val jsonPictureOfTheDayParsed = parsePictureOfTheDay(JSONObject(jsonPictureOfDayResult))
            //storing only media_type which is "image".
            if (jsonPictureOfTheDayParsed.mediaType == Constants.IMAGE) {
                //When the New picture of Day is fetched the older one is deleted from the database.
                database.pictureDao.clear()
                val dbPictureOfDay = AsteroidPictureOfDayDatabase(
                    jsonPictureOfTheDayParsed.url
                    , jsonPictureOfTheDayParsed.mediaType, jsonPictureOfTheDayParsed.title
                )
                database.pictureDao.insertAsteroidPictureOfDay(dbPictureOfDay)
            }
        }
    }

    suspend fun deleteOldAsteroidFromDatabase() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deletePreviousAsteroids(getStartDate())
        }
    }

    fun applyDatBaseFilter(filter: DataBaseFetchType) {
        _asteroidFetchType.value = filter
    }

    fun List<AsteroidDatabase>.asDomainModel(): List<Asteroid> {
        return map {
            Asteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
            )
        }
    }


    fun AsteroidPictureOfDayDatabase.asDomainModel(): PictureOfDay {
        return PictureOfDay(
            mediaType = this.mediaType,
            title = this.title,
            url = this.url
        )
    }

}