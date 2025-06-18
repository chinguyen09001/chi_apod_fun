package com.example.chiapodfun.repo

import com.example.chiapodfun.model.ApodResponse
import com.example.chiapodfun.network.NasaApi
import javax.inject.Inject

class ApodRepository @Inject constructor(private val api: NasaApi) {
    suspend fun getApod(date: String): ApodResponse {
        return api.getApod(date = date)
    }
}