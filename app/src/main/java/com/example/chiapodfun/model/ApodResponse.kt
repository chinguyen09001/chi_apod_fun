package com.example.chiapodfun.model

data class ApodResponse(
    val date: String,
    val explanation: String,
    val hdurl: String?,
    val title: String,
    val url: String
)