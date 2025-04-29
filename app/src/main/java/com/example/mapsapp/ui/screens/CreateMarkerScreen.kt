package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.mapsapp.viewmodels.ViewModelApp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MarkerScreen(lat: Double, long: Double, viewModelApp: ViewModelApp, navigateToDetailMarker: (String) -> Unit) {
    // Obtenemos los valores de name y description desde el ViewModel
    val name by viewModelApp.namePlace.observeAsState("")
    val description by viewModelApp.description.observeAsState("")

    // Estructura principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TextField para el nombre
        TextField(
            value = name,
            onValueChange = { viewModelApp.setName(it) },
            label = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        // TextField para la descripción
        TextField(
            value = description,
            onValueChange = { viewModelApp.setDescription(it) },
            label = { Text(text = "Description") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Botón para crear el marcador
        Button(
            onClick = {
                //qual clickem s'afegeix a la bdd
                viewModelApp.insertNewMarker(name, description, lat, long, foto="xd")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Marker")
        }
    }
}
