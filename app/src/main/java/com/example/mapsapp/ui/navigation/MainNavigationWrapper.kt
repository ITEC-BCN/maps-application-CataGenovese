package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.PermissionsScreen
import com.example.mapsapp.viewmodels.ViewModelApp

//comprueba permisos, si los tenemos navega a la pantalla drawer
@Composable
fun Navigation(viewModelApp: ViewModelApp){
    val navController = rememberNavController()
    NavHost(navController, Destination.Permissions) {
        composable<Destination.Permissions> {
            PermissionsScreen{
                navController.navigate(Destination.Drawer)
            }
        }
        composable<Destination.Drawer> {
            DrawerScreen(viewModelApp)
        }
    }
}
