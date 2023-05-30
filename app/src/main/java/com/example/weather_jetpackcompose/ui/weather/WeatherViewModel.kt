package com.example.weather_jetpackcompose.ui.weather


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_jetpackcompose.data.model.errorhandling.ResultOf
import com.example.weather_jetpackcompose.data.model.location.GeoModel
import com.example.weather_jetpackcompose.data.model.weather.WeatherModelModel
import com.example.weather_jetpackcompose.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _weather = MutableStateFlow<ResultOf<WeatherModelModel>>(ResultOf.Loading<WeatherModelModel>())
    val weatherDetails: StateFlow<ResultOf<WeatherModelModel>> = _weather.asStateFlow()

    private val _weatherHistory = MutableStateFlow<ResultOf<List<WeatherModelModel>>>(ResultOf.Loading<List<WeatherModelModel>>())
    val weatherHistory: StateFlow<ResultOf<List<WeatherModelModel>>> = _weatherHistory.asStateFlow()

    fun getWeather(city: String, context: Context) {
        _weather.value = ResultOf.Loading<WeatherModelModel>()
        viewModelScope.launch {
            try {
                val result = repository.getWeather(city)
                _weather.value = ResultOf.Success(result)
                // On Success of Search add city to Shared Preferences
               // saveSearchHistory(context, city)
            } catch (ioe: IOException) {
                _weather.value = ResultOf.Failure("[IO] error please retry", ioe)
            } catch (he: HttpException) {
                _weather.value = ResultOf.Failure("[HTTP] error please retry", he)
            } catch (e: Exception) {
                _weather.value = ResultOf.Failure("unknown error", e)
            }
        }
    }

//    private fun saveSearchHistory(context: Context, city: String) {
//        val sharedPreferences = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("last_searched_city", city)
//        editor.apply()
//    }


    fun getWeatherHistory(cityHistory: Set<String>) {
        _weatherHistory.value = ResultOf.Loading<List<WeatherModelModel>>()
        viewModelScope.launch {

            try {
                val weatherList = mutableListOf<WeatherModelModel>()

                for (item in cityHistory) {
                    val result = repository.getWeather(item)
                    weatherList.add(result)
                }

                _weatherHistory.value = ResultOf.Success(weatherList)
            } catch (ioe: IOException)
            {
                _weatherHistory.value =ResultOf.Failure("[IO] error please retry",ioe)
            }catch (he: HttpException)
            {
                _weatherHistory.value =ResultOf.Failure("[HTTP] error please retry",he)
            }
            catch (e: Exception) {
                _weatherHistory.value = ResultOf.Failure("unknown error", e)
            }
        }
    }

    fun getWeather(location: GeoModel) {

        _weather.value = ResultOf.Loading<WeatherModelModel>()
        viewModelScope.launch {

            try {

                val result = repository.getWeather(location.lat!!,location.lon!!)
                _weather.value = ResultOf.Success(result)
                //On Success of Search add city to Shared Preferences
                //saveSearchHistory(context, q)
                //mutableSetOfString.remove(searchQuery.value)
            }
            catch (ioe: IOException)
            {
                _weather.value =ResultOf.Failure("[IO] error please retry",ioe)
            }catch (he: HttpException)
            {
                _weather.value =ResultOf.Failure("[HTTP] error please retry",he)
            }
            catch (e: Exception) {
                _weather.value = ResultOf.Failure("unknown error", e)
            }
        }
    }


}