package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.network.networkModel.AsteroidNetworkPictureOfDay

/**
 * The Entity data class creates a table for Picture of the day.
 */
@Entity(tableName = "picture_table")
data class AsteroidPictureOfDayDatabase constructor(
    @PrimaryKey
    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "media_type")
    val mediaType: String,

    @ColumnInfo(name = "title")
    val title: String
)

