package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.Login
import com.example.mapsapp.ui.screens.PermissionsScreen
import com.example.mapsapp.ui.screens.RegisterScreen
import com.example.mapsapp.viewmodels.ViewModelApp

//comprueba permisos, si los tenemos navega a la pantalla drawer
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(viewModelApp: ViewModelApp) {
    val navController = rememberNavController()

    NavHost(navController, Destination.Permissions) {
        composable<Destination.Permissions> {
            PermissionsScreen {
                navController.navigate(Destination.LoginRoute)
            }
        }

        composable<Destination.RegistreRoute> {
            RegisterScreen {
                navController.navigate(Destination.Drawer)
            }
        }

        composable<Destination.LoginRoute> {
            Login(
                { navController.navigate(Destination.RegistreRoute) }, { navController.navigate(Destination.Drawer) }
            )
        }

        composable<Destination.Drawer> {
            DrawerScreen(viewModelApp) {
                navController.navigate(Destination.LoginRoute) {
                    popUpTo(Destination.Permissions) { inclusive = true }
                }
            }
        }
    }
}
