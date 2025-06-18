package com.example.chiapodfun.di

import com.example.chiapodfun.network.NasaApi
import com.example.chiapodfun.repo.ApodRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providesNasaApi(retrofit: Retrofit): NasaApi = retrofit.create(NasaApi::class.java)

    @Singleton
    @Provides
    fun providesApodRepository(api: NasaApi): ApodRepository = ApodRepository(api)
}