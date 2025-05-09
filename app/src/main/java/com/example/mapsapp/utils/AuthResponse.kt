package com.example.mapsapp.utils

sealed interface AuthResponse {
    object Success: AuthResponse
    data class Error(val message: String?): AuthResponse
}
