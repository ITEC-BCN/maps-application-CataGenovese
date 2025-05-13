package com.example.mapsapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.ViewModelApp
import com.example.mapsapp.viewmodels.ViewModelFactory

@Composable
    //navTOdRAWER
fun Login(navToRegister: () -> Unit, navToHome: () -> Unit){
    val context= LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = ViewModelFactory(SharedPreferencesHelper(context)))

}