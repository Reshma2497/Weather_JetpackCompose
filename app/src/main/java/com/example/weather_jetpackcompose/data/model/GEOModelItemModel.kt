package com.example.weather_jetpackcompose.data.model


import com.google.gson.annotations.SerializedName

data class GEOModelItemModel(
    @SerializedName("country")
    val country: String? = "",
    @SerializedName("lat")
    val lat: Double? = 0.0,
    @SerializedName("local_names")
    val localNames: LocalNamesModel? = LocalNamesModel(),
    @SerializedName("lon")
    val lon: Double? = 0.0,
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("state")
    val state: String? = ""
)