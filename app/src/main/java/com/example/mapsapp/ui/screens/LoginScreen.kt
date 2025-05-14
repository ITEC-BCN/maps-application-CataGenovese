package com.example.mapsapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.ViewModelApp
import com.example.mapsapp.viewmodels.ViewModelFactory
import androidx.compose.runtime.getValue

@Composable
    //navTOdRAWER
fun Login(navToRegister: () -> Unit, navToHome: () -> Unit){
    val context= LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = ViewModelFactory(SharedPreferencesHelper(context)))
    val authState by viewModel.authState.observeAsState()

    if(authState == AuthState.Authenticated){
        navToHome()
    }
    else{
    }

}