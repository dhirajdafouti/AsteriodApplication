package com.udacity.asteroidradar.network.api

import com.udacity.asteroidradar.constant.Constants.NASA_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate


/**
 * The interface which will interact with the Nasa Asteroid Server through Https using the Retrofit Service.
 */
interface AsteroidApi {

    //The suspend function will fetch the network asteroid data.
    @GET("neo/rest/v1/feed?&api_key=${NASA_API_KEY}")
    suspend fun getNetworkAsteroid():String

    //The suspend function will fetch the the Network Picture of the day.
    @GET("planetary/apod?&api_key=${NASA_API_KEY}")
    suspend fun getNetworkPictureOfTheDay(@Query("date")date:String):String
}