package com.example.weather_jetpackcompose.data.repository

import com.example.weather_jetpackcompose.data.model.WeatherModelModel

interface  Repository {

    suspend fun getWeather(q:String,appId:String):WeatherModelModel
}