package com.example.weather_jetpackcompose.data.repository

import com.example.weather_jetpackcompose.data.model.weather.WeatherModelModel
import com.example.weather_jetpackcompose.data.remote.ApiRequest
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    val apiRequest: ApiRequest
):Repository{

     override suspend fun getWeather(q:String): WeatherModelModel =apiRequest.getWeather(q)

    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherModelModel =apiRequest.getWeather(latitude,longitude)
}