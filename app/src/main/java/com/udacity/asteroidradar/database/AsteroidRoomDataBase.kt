package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = arrayOf(AsteroidDatabase::class, AsteroidPictureOfDayDatabase::class),
    exportSchema = false,
    version = 1
)
abstract class AsteroidRoomDataBase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureDao: PictureDao

}

//Late Init Instance of Room Data Base
private lateinit var INSTANCE: AsteroidRoomDataBase

/**
 * The method will provide the instance of Asteroid Database.
 * The instance will be initialized once it is called first time.
 * After the repeatable calls will provide the same instance, as creating the instance of Database is expensive.
 */
fun getDataBase(context: Context): AsteroidRoomDataBase {
    synchronized(AsteroidRoomDataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidRoomDataBase::class.java, "asteroidDatabase"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}

