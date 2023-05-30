package com.example.weather_jetpackcompose.data.model.weather


import com.google.gson.annotations.SerializedName

data class CloudsModel(
    @SerializedName("all")
    val all: Int? = 0
)