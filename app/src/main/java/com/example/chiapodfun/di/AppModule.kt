package com.example.chiapodfun.di

import com.example.chiapodfun.network.NasaApi
import com.example.chiapodfun.repo.ApodRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.nasa.gov/"

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create() )
        .build()

    @Singleton
    @Provides
    fun providesNasaApi(retrofit: Retrofit): NasaApi = retrofit.create(NasaApi::class.java)

    @Singleton
    @Provides
    fun providesApodRepository(api: NasaApi): ApodRepository = ApodRepository(api)
}