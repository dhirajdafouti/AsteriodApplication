package com.udacity.asteroidradar.network.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.constant.Constants.BASE_URL
import com.udacity.asteroidradar.network.api.AsteroidApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * A public Api object that exposes the lazily-instantiation of Retrofit -Service.
 */

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val logggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BASIC)

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logggingInterceptor).readTimeout(60, TimeUnit.SECONDS)
    .connectTimeout(60, TimeUnit.SECONDS).build()

object AsteroidApiService {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .baseUrl(BASE_URL).build()

    val retrofitService =
        retrofit.create(AsteroidApi::class.java)

}







