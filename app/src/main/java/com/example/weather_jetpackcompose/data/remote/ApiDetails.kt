package com.example.weather_jetpackcompose.data.remote

object ApiDetails {

    const val BASEURL="https://api.openweathermap.org"
    const val ENDPOINT="/data/2.5/weather"

    const val APP_ID="c6ee45ca13591a53f17910c1ddce2914"

    const val WEATHER_ICON = "https://openweathermap.org/img/wn/{icon_id}@2x.png"
    fun iconUrl(icon: String) = WEATHER_ICON.replace("{icon_id}", icon)
}