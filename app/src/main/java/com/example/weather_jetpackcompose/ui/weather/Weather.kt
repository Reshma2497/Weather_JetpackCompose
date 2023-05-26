package com.example.weather_jetpackcompose.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.weather_jetpackcompose.data.model.WeatherModelModel
import com.example.weather_jetpackcompose.data.remote.ApiDetails.APP_ID
import com.example.weather_jetpackcompose.ui.common.SearchBar


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
fun WeatherReport(viewModel: WeatherViewModel= hiltViewModel()){


    val context= LocalContext.current
    val searchQuery= remember{ mutableStateOf("") }
    val searchHistory = getSearchHistory(LocalContext.current)
    var mutableSetOfString: MutableSet<String> = remember { searchHistory?.toMutableSet()!!}

    //Call method to get History
    viewModel.getWeatherReportHistory(mutableSetOfString, APP_ID)

    val validSearch= remember{ mutableStateOf(false) }

    //deleteSearchHistory(context)
    val onSearchClicked = {
        // Handle the search button click here
        // You can access the current search query using the `searchQuery.value` property
        // Perform the necessary logic to fetch weather details based on the search query
        // Update the `weather` state variable with the new weather data
        viewModel.getWeatherReport(searchQuery.value,APP_ID)
        /// Save the searched city name to history
        saveSearchHistory(context, searchQuery.value)
        mutableSetOfString= getSearchHistory(context)?.toMutableSet()!!

        //remove current Search Item
        mutableSetOfString.remove(searchQuery.value)
        //Call method to get History
        viewModel.getWeatherReportHistory(mutableSetOfString, APP_ID)

        //set valid search
        validSearch.value=true
    }

    // Load searched Weather
    val weather by viewModel.weatherDetails.collectAsState()
    // Load search history
    val weatherHistory by viewModel.weatherHistory.collectAsState()

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize()

        ) {
            SearchBar(searchQuery, "Search by city name", onSearchClicked)


            if (validSearch.value) {
                Text(
                    text = "Searched Weather:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //WeatherAppScreen(weather)
                    WeatherAppCard(weather)
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

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(weatherHistory.size) { weatherItem ->
                        //WeatherAppScreen(weatherHistory[weatherItem])
                        WeatherAppCard(weatherHistory[weatherItem])
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

        }
    }


}


@Composable
fun WeatherAppScreen(weatherData: WeatherModelModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weatherData?.name!!,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberImagePainter(data = "https://openweathermap.org/img/wn/${weatherData.weather?.firstOrNull()?.icon?:"10d" }@2x.png"),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        )
        Text(
            text = weatherData.weather?.firstOrNull()?.description ?: "",
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            WeatherInfoItem(
                label = "Temperature",
                value = "${weatherData.main?.temp}°C"
            )
            WeatherInfoItem(
                label = "Feels Like",
                value = "${weatherData.main?.feelsLike}°C"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            WeatherInfoItem(
                label = "Min Temperature",
                value = "${weatherData.main?.tempMin}°C"
            )
            WeatherInfoItem(
                label = "Max Temperature",
                value = "${weatherData.main?.tempMax}°C"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        WeatherInfoItem(
            label = "Pressure",
            value = "${weatherData.main?.pressure} hPa"
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeatherInfoItem(
            label = "Humidity",
            value = "${weatherData.main?.humidity}%"
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeatherInfoItem(
            label = "Wind Speed",
            value = "${weatherData.wind?.speed} m/s"
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeatherInfoItem(
            label = "Cloudiness",
            value = "${weatherData.clouds?.all}%"
        )
    }
}

@Composable
fun WeatherInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.body2, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.body1)
    }
}

@Composable
fun WeatherAppCard(weatherData: WeatherModelModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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




