package com.example.weather_jetpackcompose.ui.weather


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_jetpackcompose.data.model.WeatherModelModel
import com.example.weather_jetpackcompose.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val repository: Repository
):ViewModel(){

    private val _weather = MutableStateFlow(WeatherModelModel())
    val weatherDetails: StateFlow<WeatherModelModel> = _weather

    private val _weatherHistory = MutableStateFlow<List<WeatherModelModel>>(emptyList())
    val weatherHistory: StateFlow<List<WeatherModelModel>> = _weatherHistory


    fun getWeatherReport(q: String, appId: String) {
        viewModelScope.launch {
            val result = repository.getWeather(q, appId)
            _weather.value=result
        }
    }

    fun getWeatherReportHistory( history:Set<String>,appId: String ) {
        viewModelScope.launch {
            val weatherList = mutableListOf<WeatherModelModel>()

            for (item in history) {
                val result = repository.getWeather(item, appId)
                weatherList.add(result)
            }

            _weatherHistory.value = weatherList

        }
    }


}