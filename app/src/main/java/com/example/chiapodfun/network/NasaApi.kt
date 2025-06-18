package com.example.chiapodfun.network

import com.example.chiapodfun.model.ApodResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "G6RNc1sPdpGd3JY4jY4yafQ54cTKObSpixWLJVPr"

interface NasaApi {
    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("date") date: String,
    ): ApodResponse
}