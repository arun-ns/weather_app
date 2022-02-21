package com.sunnyday.network

import com.sunnyday.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET

interface ApiClient {
    //Events
    @GET("/api/forecast")
    fun getWeatherData(): Call<List<WeatherData>>
}