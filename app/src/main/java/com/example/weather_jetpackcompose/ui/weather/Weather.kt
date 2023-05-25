package com.example.weather_jetpackcompose.ui.weather

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weather_jetpackcompose.data.model.WeatherModelModel
import com.example.weather_jetpackcompose.ui.common.SearchBar


@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherReport(viewModel: WeatherViewModel= hiltViewModel()){

    val weatherReport = remember{ mutableStateListOf<WeatherModelModel>() }

    val searchQuery= remember{ mutableStateOf("") }
    val filteredWeatherReport=if(searchQuery.value.isBlank()){
        weatherReport
    } else{
        weatherReport.filter { weatherReport ->
            weatherReport.name!!.contains(searchQuery.value, ignoreCase = true)
        }
    }

    viewModel.getWeatherReport("London,uk","b7cd35b3515aa50f6719006df7595f39")

    val w=viewModel.weather.observeAsState().value



    Scaffold(
    ) {
        Column (
            modifier=Modifier
                .fillMaxWidth()) {
            SearchBar(searchQuery, "Search by city name")

          WeatherReport(w)
        }

    }


}

@Composable
fun WeatherReport(weather:WeatherModelModel?){
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = weather?.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = weather?.base ?: "",
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
        }
    }
}

