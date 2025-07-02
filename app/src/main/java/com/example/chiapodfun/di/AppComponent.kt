package com.example.chiapodfun.di

import com.example.chiapodfun.ui.MainActivity
import com.example.chiapodfun.ui.ApodFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: ApodFragment)
}