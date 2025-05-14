package com.example.mapsapp.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(navToHome: () -> Unit) {
    val viewModelAuth: AuthViewModel = viewModel()
    val authState by viewModelAuth.authState.observeAsState()
    val showError by viewModelAuth.showError.observeAsState(false)

    if (authState == AuthState.Authenticated) {
        navToHome()
    } else {
        if (showError) {
            val errorMessage = (authState as AuthState.Error).message
            if (errorMessage!!.contains("invalid_credentials")) {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "An error has ocurred", Toast.LENGTH_LONG).show()
            }
            viewModelAuth.errorMessageShowed()
        }
    }
}