package com.example.weather_jetpackcompose.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_jetpackcompose.data.model.WeatherModelModel
import com.example.weather_jetpackcompose.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val repository: Repository
):ViewModel(){

    val weather= MutableLiveData<WeatherModelModel>()

    fun getWeatherReport(q: String, appId: String) {
        viewModelScope.launch {
            val result = repository.getWeather(q, appId)
            weather.postValue(result)
        }
    }

}