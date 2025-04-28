package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.mapsapp.viewmodels.ViewModelApp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import io.ktor.websocket.Frame.Text

@Composable
fun MarkerScreen(coordenades: String, viewModelApp: ViewModelApp, navigateToDetailMarker: (String) -> Unit) {
    val name by viewModelApp.namePlace.observeAsState("")
    val description by viewModelApp.description.observeAsState(" ")

    //funcio per guardar el nom
    TextField(
        value = name,
        onValueChange = { viewModelApp.setName(it) },
        label = { Text(text = "Name") }, modifier = Modifier.fillMaxSize()
    )
    //funcion per guardar la descripció
    TextField(
        value = description,
        onValueChange = { viewModelApp.setDescription(it) },
        label = { Text(text = "Description") }
    )

    Button(onClick = {

    }) {
        Text("Marker")
    }


}