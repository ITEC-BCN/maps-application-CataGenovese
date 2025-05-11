package com.example.mapsapp.data

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val lat: Double,
    val long: Double,
    val foto: String?
)
