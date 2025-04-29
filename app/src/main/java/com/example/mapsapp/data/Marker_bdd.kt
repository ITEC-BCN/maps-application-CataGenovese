package com.example.mapsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Marker_bdd(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val foto: String = "fotoCataiSanti"
)
