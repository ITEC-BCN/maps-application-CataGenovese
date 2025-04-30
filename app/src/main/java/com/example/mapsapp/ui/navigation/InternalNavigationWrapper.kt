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
import org.slf4j.Marker

@Composable
fun InternalNavigation(
    navController: NavHostController,
    padding: Modifier,
    viewModelApp: ViewModelApp,
    modifier: Modifier,
) {
    NavHost(navController, Destination.Map) {
        //mapa
        composable<Destination.Map> {
            MapsScreen(modifier, { id ->
                navController.navigate(Destination.MarkerDetail(id))
            }) { lat, long ->
                navController.navigate(Destination.MarkerCreation(lat, long))
            }
        }

        //listScreen marcadores
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
                navController.navigate(Destination.List) {
                    popUpTo<Destination.List> { inclusive = true }
                }
            }
        }
    }
}

