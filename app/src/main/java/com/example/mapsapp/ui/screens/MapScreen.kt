package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    navigateToMarkerScreen: (Double, Double) -> Unit
) {
    val vM = viewModel<ViewModelApp>()
    val markerList by vM.markerList.observeAsState(emptyList())
    vM.getAllMarkers()

    Column(modifier.fillMaxSize()) {
        val itb = LatLng(41.4534225, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }
        GoogleMap(
            modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            //clickem i navega al marker screen
            onMapLongClick = {
                Log.d("XD", "hola")
                navigateToMarkerScreen(it.latitude, it.longitude)
            }) {

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
                    }
                )
            }
        }

    }
}

