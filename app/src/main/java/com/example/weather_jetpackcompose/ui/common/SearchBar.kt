package com.example.weather_jetpackcompose.ui.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weather_jetpackcompose.ui.theme.SkyBlue

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun SearchBar(

    searchQuery: MutableState<String>, searchPlaceholder:String, modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery.value,
        onValueChange = { searchQuery.value = it },
        leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )

        },
        colors = TextFieldDefaults.textFieldColors(
//            backgroundColor = Color.White,
            focusedIndicatorColor = SkyBlue, // Change this to the desired border color
            //unfocusedIndicatorColor = Color.Transparent // Change this to the desired border color
        ),
        placeholder = {
            Text( searchPlaceholder)

        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),

        )
}