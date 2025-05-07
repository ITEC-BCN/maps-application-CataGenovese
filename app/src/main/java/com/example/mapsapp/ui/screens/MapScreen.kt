package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.ViewModelApp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment

@Composable
fun MapsScreen(
    navigateToDetail: (Int) -> Unit,
    navigateToMarkerScreen: (Double, Double) -> Unit,
) {
    val vM = viewModel<ViewModelApp>()
    val markerList by vM.markerList.observeAsState(emptyList())
    val mapTypes = listOf("Normal", "Satellite", "Hybrid", "Terrain")
    val selectedMapType by vM.tipusMapa.observeAsState("Normal")
    val expanded by vM.expanded.observeAsState(false)

    val mapProperties = remember(selectedMapType) {
        MapProperties(
            isMyLocationEnabled = true,
            mapType = when (selectedMapType) {
                "Satellite" -> MapType.SATELLITE
                "Hybrid" -> MapType.HYBRID
                "Terrain" -> MapType.TERRAIN
                else -> MapType.NORMAL
            }
        )
    }

    vM.getAllMarkers()

    Column(modifier = Modifier.fillMaxSize() .background(color = Color(0xFFF3EEE3))) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            MapTypeDropdownInMap(
                expanded = expanded,
                selectedMapType = selectedMapType,
                onMapTypeSelected = { type ->
                    vM.setMapa(type)
                    vM.setExpanded(false)
                },
                mapTypes = mapTypes,
                onExpandedChange = { newExpandedState ->
                    vM.setExpanded(newExpandedState)
                },
            )
        }

        val itb = LatLng(41.4534225, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            onMapLongClick = {
                Log.d("XD", "hola")
                navigateToMarkerScreen(it.latitude, it.longitude)
            }
        ) {
            Marker(
                state = MarkerState(position = itb),
                title = "ITB",
                snippet = "Marker at ITB"
            )

            markerList.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.lat, marker.long)),
                    title = marker.name,
                    snippet = marker.description,
                    onClick = {
                        navigateToDetail(marker.id)
                        true
                    },
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTypeDropdownInMap(
    expanded: Boolean,
    selectedMapType: String,
    onMapTypeSelected: (String) -> Unit,
    mapTypes: List<String>,
    onExpandedChange: (Boolean) -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) },
        modifier = Modifier
            .padding(8.dp)
    ) {
        TextField(
            value = selectedMapType,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo de mapa", color = Color.Black) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .padding(4.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            mapTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onMapTypeSelected(type)
                    }
                )
            }
        }
    }
}

