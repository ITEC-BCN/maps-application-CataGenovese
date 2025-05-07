package com.example.mapsapp.ui.navigation

import MarkerDetail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.ListMarkers
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerScreen
import com.example.mapsapp.viewmodels.ViewModelApp
import com.google.maps.android.compose.MapProperties
import org.slf4j.Marker

@Composable
fun InternalNavigation(
    navController: NavHostController,
    viewModelApp: ViewModelApp,
) {
    NavHost(navController, Destination.Map) {
        //mapa -> edit marker

        composable<Destination.Map> {
            MapsScreen( { id ->
                navController.navigate(Destination.MarkerDetail(id))
            }) { lat, long ->
                navController.navigate(Destination.MarkerCreation(lat, long))
            }
        }

        //crear marker ->
        composable<Destination.MarkerCreation> { backStackEntry ->
            val markerCreation = backStackEntry.toRoute<Destination.MarkerCreation>()
            MarkerScreen(
                lat = markerCreation.lat,
                long = markerCreation.long,
                viewModelApp = viewModelApp
            ) {
                navController.navigate(Destination.Map) {
                    popUpTo<Destination.Map> { inclusive = true }
                }
            }
        }
        composable<Destination.List> {
            ListMarkers { markerId ->
                navController.navigate(Destination.MarkerDetail(markerId))
            }
        }

        composable<Destination.MarkerDetail> { id ->
            val detail = id.toRoute<Destination.MarkerDetail>()
            MarkerDetail(detail.id) {
                navController.popBackStack()
            }
        }
    }
}

