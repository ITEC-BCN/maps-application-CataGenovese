package com.example.mapsapp.ui.navigation

import MarkerDetail
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.ListMarkers
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerScreen
import com.example.mapsapp.viewmodels.ViewModelApp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InternalNavigation(
    navController: NavHostController,
    viewModelApp: ViewModelApp,
) {
    NavHost(navController, Destination.Map) {
        //mapa -> edit marker

        composable<Destination.Map> {
            MapsScreen(viewModelApp, { id ->
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
            )
        }
        composable<Destination.List> {
            ListMarkers(viewModelApp) { markerId ->
                navController.navigate(Destination.MarkerDetail(markerId))
            }
        }

        composable<Destination.MarkerDetail> { id ->
            val detail = id.toRoute<Destination.MarkerDetail>()
            MarkerDetail(viewModelApp, detail.id) {
                navController.popBackStack()
            }
        }
    }
}

