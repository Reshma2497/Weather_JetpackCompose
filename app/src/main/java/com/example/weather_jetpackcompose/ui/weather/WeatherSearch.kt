package com.example.weather_jetpackcompose.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter

import com.example.weather_jetpackcompose.data.model.errorhandling.ResultOf

import com.example.weather_jetpackcompose.data.model.weather.WeatherModelModel
import com.example.weather_jetpackcompose.ui.common.Alert
import com.example.weather_jetpackcompose.ui.common.CircularProgressBar
import com.example.weather_jetpackcompose.ui.common.SearchBar
import com.example.weather_jetpackcompose.ui.weather.WeatherViewModel

private const val PREF_NAME = "WeatherAppPrefs"
private const val HISTORY_KEY = "searchHistory"


fun deleteSearchHistory(context: Context)
{
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    // Clear all data from SharedPreferences
    editor.clear()

// Apply the changes
    editor.apply()
}
fun saveSearchHistory(context: Context, cityName: String) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Get the existing search history
    val searchHistory = sharedPreferences.getStringSet(HISTORY_KEY, HashSet())?.toMutableSet()

    // Add the new city name to the history
    searchHistory?.add(cityName)

    // Save the updated history
    editor.putStringSet(HISTORY_KEY, searchHistory)
    editor.apply()
}
fun getSearchHistory(context: Context): Set<String>? {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Return the search history from SharedPreferences
    return sharedPreferences.getStringSet(HISTORY_KEY, HashSet())
}


@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearch(viewModel: WeatherViewModel = hiltViewModel()){

    val context= LocalContext.current
   // deleteSearchHistory(context)
    val searchQuery= remember{ mutableStateOf("") }
    val searchHistory = getSearchHistory(context)
    var mutableSetOfString: MutableSet<String> = remember { searchHistory?.toMutableSet()!!}
    //To Load Only Once
    LaunchedEffect(true) {
        viewModel.getWeatherHistory(mutableSetOfString)
    }
    val validSearch= remember{ mutableStateOf(false) }
    //deleteSearchHistory(context)
    val onSearchClicked = {
        viewModel.getWeather(searchQuery.value,context)
        validSearch.value = true
        //Get History After Search to refresh
       // mutableSetOfString= getSearchHistory(context)?.toMutableSet()!!
        //Remove Current Searched City
        //mutableSetOfString.remove(searchQuery.value)
        // Call method to get History
       // viewModel.getWeatherHistory(mutableSetOfString)
        // Set valid search

    }
    // Load searched Weather
    var weather =viewModel.weatherDetails.collectAsState().value
    var weatherHistory = viewModel.weatherHistory.collectAsState().value


    // Observe weather details
    LaunchedEffect(weather) {
        if (weather is ResultOf.Failure) {
            validSearch.value = false
            //Alert("Error while getting weather details", weather.message ?: "Unknown error")
        } else if (weather is ResultOf.Success) {
            val city = searchQuery.value
            if (city.isNotBlank()) {
                saveSearchHistory(context, city)
                mutableSetOfString=getSearchHistory(context)?.toMutableSet()!!
                viewModel.getWeatherHistory(mutableSetOfString)
            }
        }
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF64B5F6))
                .padding(16.dp),

            ) {
            SearchBar(searchQuery, "Search by city name", onSearchClicked)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //WeatherAppScreen(weather)
                    when(weather)
                    {
                        is ResultOf.Loading ->{
                            if(validSearch.value) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressBar(modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 3))
                                }
                            }
                        }
                        is ResultOf.Success-> {
                            //saveSearchHistory(context,searchQuery.value)
                            Text(
                                text = "Searched Weather:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                            )
                            WeatherAppCard(weather.value)
                        }
                        is ResultOf.Failure -> {
                            validSearch.value = false
                            Alert("Error while getting weather details", weather?.message?:"Unknown error")
                        }
                    }

                }
            // Display search history
            if (mutableSetOfString != null && mutableSetOfString.isNotEmpty()) {
                Text(
                    text = "Search History:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
                when(weatherHistory)
                {
                    is ResultOf.Loading ->{
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressBar(modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 3))
                        }
                    }
                    is ResultOf.Success-> {

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            items(weatherHistory.value.size) { weatherItem ->
                                //WeatherAppScreen(weatherHistory[weatherItem])
                                WeatherAppCard(weatherHistory.value[weatherItem])
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                    is ResultOf.Failure -> {
                        Alert("Error while getting History weather details", weatherHistory?.message?:"Unknown error")
                    }
                }


            }

        }
    }


}


@Composable
fun WeatherAppCard(weatherData: WeatherModelModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = weatherData.name ?: "",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = rememberImagePainter(
                        data = "https://openweathermap.org/img/wn/${weatherData.weather?.firstOrNull()?.icon ?: "10d"}@2x.png"
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                WeatherInfoItem(
                    label = "Temperature",
                    value = "${weatherData.main?.temp}°C"
                )
                Spacer(modifier = Modifier.height(8.dp))
                WeatherInfoItem(
                    label = "Feels Like",
                    value = "${weatherData.main?.feelsLike}°C"
                )
                Spacer(modifier = Modifier.height(8.dp))
                WeatherInfoItem(
                    label = "Humidity",
                    value = "${weatherData.main?.humidity}%"
                )
            }
        }
    }
}

