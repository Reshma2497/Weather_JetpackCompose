package com.example.weather_jetpackcompose.data.model.weather


import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("icon")
    val icon: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("main")
    val main: String? = ""
)