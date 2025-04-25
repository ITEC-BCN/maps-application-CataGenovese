package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.PermissionsScreen

@Composable
fun InternalNavigation(navController: NavHostController, padding: Modifier) {
    NavHost(navController, Destination.Map) {
        composable<Destination.Map> {
            MapsScreen()
        }
    }
}