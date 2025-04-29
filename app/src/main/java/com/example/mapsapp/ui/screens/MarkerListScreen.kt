package com.example.mapsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Marker_bdd
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.mapsapp.viewmodels.ViewModelApp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.ui.draw.shadow


@Composable
fun ListMarkers(navigateToDetail: (Int) -> Unit) {
    val vM = viewModel<ViewModelApp>()
    val markerList by vM.markerList.observeAsState(emptyList<Marker_bdd>())
    vM.getAllMarkers()
    val markerName: String by vM.namePlace.observeAsState(" ")
    val description: String by vM.description.observeAsState("")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 120.dp)
    ) {

        Text(
            text = "Markers List",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp),
            color = Color.DarkGray
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        {
            items(markerList) { marker ->
                val dissmissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            vM.deleteMarker(marker.id)
                            true
                        } else {
                            false
                        }
                    }
                )
                SwipeToDismissBox(state = dissmissState, backgroundContent = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }) {
                    MarkerItem(marker) {
                        navigateToDetail(marker.id)
                    }
                }
            }
        }
    }
}


@Composable
fun MarkerItem(marker: Marker_bdd, navigateToDetail: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = Color.LightGray)
            .clickable { navigateToDetail(marker.id) }
            .padding(16.dp)
    ) {
        Text(
            text = marker.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }
}