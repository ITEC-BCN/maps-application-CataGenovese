package com.example.mapsapp.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mapsapp.ui.navigation.Destination

enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val route: Destination
) {
    Map(Icons.Default.Place, "Map", Destination.Map),
    List(Icons.Default.List, "List", Destination.List),
}
