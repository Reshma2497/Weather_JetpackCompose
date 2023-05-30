package com.example.weather_jetpackcompose.data.repository

import com.example.weather_jetpackcompose.data.model.weather.WeatherModelModel

interface  Repository {

    suspend fun getWeather(q:String): WeatherModelModel

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherModelModel
}