package com.udacity.asteroidradar.network.networkModel

import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidPictureOfDayDatabase

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

data class NetworkAsteroidsContainer(val asteroids: List<AsteroidNetworkData>)


data class NetworkPictureOfDayContainer(val pictureOfDay: AsteroidNetworkPictureOfDay)


data class AsteroidNetworkData(
    val id: Long = 0L,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)


data class AsteroidNetworkPictureOfDay(
    val title: String,
    val url: String,
    val mediaType: String
)

fun NetworkAsteroidsContainer.asDatabaseModel(): Array<AsteroidDatabase> {
    return asteroids.map {
        AsteroidDatabase(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}


/**
 * Extension function which Convert Network results to Database Objects.
 */
fun NetworkPictureOfDayContainer.asDatabaseModel(): AsteroidPictureOfDayDatabase {
    return AsteroidPictureOfDayDatabase(
        url = pictureOfDay.url,
        mediaType = pictureOfDay.mediaType,
        title = pictureOfDay.title
    )
}

