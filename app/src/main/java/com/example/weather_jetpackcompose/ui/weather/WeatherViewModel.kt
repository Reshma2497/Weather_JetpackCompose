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


    fun getWeatherReport(q: String, appId: String) {
        viewModelScope.launch {
            val result = repository.getWeather(q, appId)
            _weather.value=result
        }
    }

}