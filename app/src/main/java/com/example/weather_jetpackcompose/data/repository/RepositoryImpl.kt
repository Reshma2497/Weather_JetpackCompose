package com.example.weather_jetpackcompose.data.repository

import com.example.weather_jetpackcompose.data.model.WeatherModelModel
import com.example.weather_jetpackcompose.data.remote.ApiRequest
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    val apiRequest: ApiRequest
):Repository{

     override suspend fun getWeather(q:String,appId:String): WeatherModelModel=apiRequest.getWeather(q, appId)
}