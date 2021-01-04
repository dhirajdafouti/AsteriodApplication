package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * The interface which will interact with Database to get the for the  Asteroid Picture of the Day.
 */
@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroidPictureOfDay(vararg pictureOfDayDatabase: AsteroidPictureOfDayDatabase)

    @Query("select *FROM picture_table")
    fun getAsteroidPictureOfDay(): LiveData<AsteroidPictureOfDayDatabase>

    @Query("DELETE FROM picture_table")
    fun clear()
}
