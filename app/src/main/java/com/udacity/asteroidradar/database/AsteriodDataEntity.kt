package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.network.networkModel.AsteroidNetworkData

/**
 * Entity class for the which defines the Asteroid Table in the Asteroid DataBase.
 */
@Entity(tableName = "asteroidtable")
data class AsteroidDatabase constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "code_name")
    val codename: String,

    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,

    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,

    @ColumnInfo(name = "estimate_diameter")
    val estimatedDiameter: Double,

    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,

    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,

    @ColumnInfo(name = "isPotentially_harzardous")
    val isPotentiallyHazardous: Boolean
)
