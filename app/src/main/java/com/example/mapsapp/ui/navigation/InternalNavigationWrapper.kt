package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerScreen
import com.example.mapsapp.viewmodels.ViewModelApp

@Composable
fun InternalNavigation(navController: NavHostController, padding: Modifier) {
    NavHost(navController, Destination.Map) {
        //mapa
        composable<Destination.Map> {
            MapsScreen()
        }

        composable<Destination.MarkerCreation> { backStackEntry ->
            val markerCreation = backStackEntry.toRoute<Destination.MarkerCreation>()
            MarkerScreen(markerCreation.coordenades, viewModelApp = ViewModelApp()) {
                navController.navigate(Map) {
                    popUpTo<Map>{inclusive=true}
                }
            }
        }
    }
}