package com.example.mapsapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mapsapp.utils.SharedPreferencesHelper

class ViewModelFactory(private val shredPreferences: SharedPreferencesHelper): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelApp::class.java)) {
            return ViewModelApp(shredPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}