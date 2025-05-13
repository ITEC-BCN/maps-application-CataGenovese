package com.example.mapsapp.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {



    //permissos
    @Serializable
    object Permissions: Destination()

    @Serializable
    object LoginRoute: Destination()

    @Serializable
    object RegistreRoute: Destination()

    //tenim el menu
    @Serializable
    object Drawer: Destination()

    //mapa
    @Serializable
    object Map: Destination()

    //llista de markers
    @Serializable
    object List: Destination()

    //creador dels markers
    @Serializable
    data class MarkerCreation(var lat: Double, var long: Double): Destination()

    //details
    @Serializable
    data class MarkerDetail(val id: Int): Destination()

}