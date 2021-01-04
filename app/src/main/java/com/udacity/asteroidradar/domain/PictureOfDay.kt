package com.udacity.asteroidradar.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 * he below data class represent the Picture of the day.
 */

data class PictureOfDay(
    @Json(name = "media_type") val mediaType: String, val title: String,
    val url: String
)