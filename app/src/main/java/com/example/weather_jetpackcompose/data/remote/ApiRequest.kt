package com.example.weather_jetpackcompose.data.remote

import com.example.weather_jetpackcompose.data.model.weather.WeatherModelModel
import com.example.weather_jetpackcompose.data.remote.ApiDetails.APP_ID
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET(ApiDetails.ENDPOINT)
    suspend fun getWeather(@Query("q") q:String, @Query("APPID") appId:String =APP_ID): WeatherModelModel

    @GET(ApiDetails.ENDPOINT)
    suspend fun getWeather(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("appid") appId: String = APP_ID): WeatherModelModel

}