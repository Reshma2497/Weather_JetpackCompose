package com.example.weather_jetpackcompose.data.remote

import com.example.weather_jetpackcompose.data.model.WeatherModelModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET(ApiDetails.ENDPOINT)
    suspend fun getWeather(@Query("q") q:String, @Query("APPID") appId:String): WeatherModelModel
}