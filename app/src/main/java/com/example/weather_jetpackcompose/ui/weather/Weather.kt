package com.example.weather_jetpackcompose.ui.weather

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.weather_jetpackcompose.R
import com.example.weather_jetpackcompose.data.model.WeatherModelModel
import com.example.weather_jetpackcompose.data.remote.ApiDetails
import com.example.weather_jetpackcompose.data.remote.ApiDetails.APP_ID
import com.example.weather_jetpackcompose.ui.common.SearchBar
import com.example.weather_jetpackcompose.ui.theme.DarkBlue
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.TimeZone



@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherReport(viewModel: WeatherViewModel= hiltViewModel()){


    val searchQuery= remember{ mutableStateOf("") }


//   if(searchQuery.value=="")
//   {
//       searchQuery.value="london"
//       viewModel.getWeatherReport(searchQuery.value,APP_ID)
//   }


    //Search On click

     val onSearchClicked = {
        // Handle the search button click here
        // You can access the current search query using the `searchQuery.value` property
        // Perform the necessary logic to fetch weather details based on the search query
        // Update the `weather` state variable with the new weather data
         viewModel.getWeatherReport(searchQuery.value,APP_ID)
    }
    val weather by viewModel.weatherDetails.collectAsState()
    Scaffold(
    ) {
        Column (
            modifier=Modifier
                .fillMaxWidth()) {
            SearchBar(searchQuery, "Search by city name", onSearchClicked)
            if(searchQuery.value!="") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //WeatherReport(weather)
                    WeatherAppScreen(weather)
                }
            }
        }

    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherReport(weather:WeatherModelModel?) {
//    Card(
//        modifier = Modifier
//            .padding(horizontal = 16.dp, vertical = 4.dp)
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp)
//    ){
        Log.d("MainViewModel", "temp:::::    ${weather?.main!!.temp?.toInt().toString()}")
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .fillMaxWidth()
        ){
            Text(
                text = "${weather.main.temp!!.toInt()}°",
                color = DarkBlue,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = weather?.name?.let { "$it (${weather.sys!!.country})" }
                    ?: "",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                //text = weather?.weather!![0]!!.description.toString(),
                text = "abc",
                color = DarkBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Timezone: ${applyTimeZone(weather?.timezone!!)}")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Air Pressure: ${(weather.main!!.pressure).toString()} mb")

            Text(text = "Humidity: ${(weather.main!!.humidity).toString()}%")
            Text(text = "Visibility: ${(weather.visibility!! / 1000)} km")

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sunrise: ${
                    toDisplayTime(weather.sys!!.sunrise!!.toLong() )
                }"
            )

            Text(
                text = "Sunset: ${
                    toDisplayTime(weather.sys!!.sunset!!.toLong() )
                }"
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Wind Speed:  ${(weather.wind!!.speed)} km in ${weather.wind!!.deg}°")


        }
    }

//    Column {
//        val listState = rememberLazyListState()
//        LazyRow(state = listState) {
//            itemsIndexed(weather?.weather ?: ArrayList()) { index, item ->
//                item?.let {
//                    AsyncImage(
//                        model = ApiDetails.iconUrl(it.icon!!),
//                        contentDescription = it.description,
//                        modifier = Modifier
//                            .size(180.dp)
//                    )
//                }
//            }
//        }
//    }


//}


fun applyTimeZone(timeZone: Int): String {
    val fromGmt = timeZone.toDouble() / (60 * 60)
    return "${if (fromGmt > 0) "GMT +" else "GMT -"} ${Math.abs(fromGmt)}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun toDisplayTime(time: Long): String =
    LocalDateTime.ofInstant(
        Instant.ofEpochMilli(time * 1000),
        TimeZone
            .getDefault().toZoneId()
    ).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"))


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

//fun getWeatherIconResourceId(icon: String?): Int {
//    return when (icon) {
//        "01d" -> R.drawable.ic_clear_sky
//        "01n" -> R.drawable.ic_clear_sky_night
//        "02d" -> R.drawable.ic_few_clouds
//        "02n" -> R.drawable.ic_few_clouds_night
//        "03d", "03n", "04d", "04n" -> R.drawable.ic_cloudy
//        "09d", "09n" -> R.drawable.ic_shower_rain
//        "10d", "10n" -> R.drawable.ic_rain
//        "11d", "11n" -> R.drawable.ic_thunderstorm
//        "13d", "13n" -> R.drawable.ic_snow
//    }
//}

