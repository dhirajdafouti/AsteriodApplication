package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * The interface which will interact with Database to get the for the Database Asteroid  Data.
 */
@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroidData(vararg asteroidDatabase: AsteroidDatabase)

    @Query("SELECT *FROM asteroidtable ORDER BY close_approach_date DESC")
    fun getAllAsteroids(): LiveData<List<AsteroidDatabase>>

    @Query("SELECT * FROM asteroidtable WHERE close_approach_date = :date ORDER BY close_approach_date DESC")
    fun getTodayAsteroids(date: String): LiveData<List<AsteroidDatabase>>

    @Query("SELECT * FROM asteroidtable WHERE close_approach_date BETWEEN :startDate AND :endDate")
    fun getWeeklyAsteroids(startDate: String, endDate: String): LiveData<List<AsteroidDatabase>>

    @Query("DELETE FROM asteroidtable WHERE close_approach_date < :todayDate ")
    fun deletePreviousAsteroids(todayDate:String)
}