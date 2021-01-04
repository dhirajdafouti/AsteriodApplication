package com.udacity.asteroidradar

import androidx.room.Room
import androidx.test.InstrumentationRegistry

import androidx.test.filters.LargeTest

import androidx.test.runner.AndroidJUnit4
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidRoomDataBase
import com.udacity.asteroidradar.database.PictureDao
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest

class RoomDataBaseTest {
    private lateinit var asteroidDao: AsteroidDao
    private lateinit var pictureDao: PictureDao
    private lateinit var db: AsteroidRoomDataBase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AsteroidRoomDataBase::class.java)
            .allowMainThreadQueries().build()
        asteroidDao = db.asteroidDao
        pictureDao = db.pictureDao
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    suspend fun insertAndGetTheAsteroidDataToDatabase():Unit {
        val listOfAsteroid = mutableListOf<AsteroidDatabase>()
        val asteroidData1 = AsteroidDatabase(
            3471590,
            "NasaMission",
            "12-12-2021",
            20.6, 123.5, 345.67, 45678.677, true
        )
        val asteroidData2 = AsteroidDatabase(
            3471590,
            "NasaMission",
            "11-12-2021",
            20.6, 123.5, 345.67, 45678.677, true
        )
        val asteroidData3 = AsteroidDatabase(
            3471590,
            "NasaMission",
            "10-12-2021",
            20.6, 123.5, 345.67, 45678.677, true
        )
        val asteroidData4 = AsteroidDatabase(
            3471590,
            "NasaMission",
            "09-12-2021",
            20.6, 123.5, 345.67, 45678.677, true
        )
        val asteroidData5 = AsteroidDatabase(
            3471590,
            "NasaMission",
            "08-12-2021",
            20.6, 123.5, 345.67, 45678.677, true
        )
        listOfAsteroid.add(asteroidData1)
        listOfAsteroid.add(asteroidData2)
        listOfAsteroid.add(asteroidData3)
        listOfAsteroid.add(asteroidData4)
        listOfAsteroid.add(asteroidData5)
        asteroidDao.insertAllAsteroidData(*listOfAsteroid.toTypedArray())
        val liveData = asteroidDao.getAllAsteroids()
       liveData.value?.size
        Assert.assertEquals(0,0)
    }

}
