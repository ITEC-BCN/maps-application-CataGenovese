package com.example.mapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mapsapp.ui.navigation.Navigation
import com.example.mapsapp.ui.screens.ListMarkers
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.viewmodels.ViewModelApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModelApp: ViewModelApp by viewModels()

        setContent {
            MapsAppTheme {
                Navigation(viewModelApp)
                }
            }
        }
    }

