package com.example.mapsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.ui.navigation.Navigation
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelFactory
import com.example.mapsapp.viewmodels.ViewModelApp

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsAppTheme {
                val context = LocalContext.current
                val viewModel: ViewModelApp = viewModel(factory = ViewModelFactory(SharedPreferencesHelper(context)))
                Navigation(viewModel)
            }
        }
    }
}

