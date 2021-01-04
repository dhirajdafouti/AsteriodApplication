package com.udacity.asteroidradar.util

import android.annotation.SuppressLint
import com.udacity.asteroidradar.constant.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.networkModel.AsteroidNetworkData
import com.udacity.asteroidradar.network.networkModel.NetworkAsteroidsContainer
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<AsteroidNetworkData> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<AsteroidNetworkData>()

    val nextSevenDaysFormattedDates =
        getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")

            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val asteroid = AsteroidNetworkData(
                id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
            )
            asteroidList.add(asteroid)
        }
    }
    NetworkAsteroidsContainer(asteroidList)
    return asteroidList
}
@SuppressLint("WeekBasedYear")
 fun getStartDate(): String {
    val calendar = Calendar.getInstance()
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime)
}


@SuppressLint("WeekBasedYear")
 fun getEndDate(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime)
}

@SuppressLint("WeekBasedYear")
private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return formattedDateList
}

fun parsePictureOfTheDay(jsonResult: JSONObject): PictureOfDay {
    val url = jsonResult.optString("url")
    val tile: String
    val mediaType = jsonResult.optString("media_type")
    tile = jsonResult.optString("title")
    val pictureofDay = PictureOfDay(mediaType, tile, url)
    return pictureofDay
}