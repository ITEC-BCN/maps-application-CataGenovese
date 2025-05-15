package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.viewmodels.AuthViewModel
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.ViewModelFactory

@Composable
fun RegisterScreen(navToHome: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferencesHelper = SharedPreferencesHelper(context)
    val factory = ViewModelFactory(sharedPreferencesHelper)
    val viewModelAuth: AuthViewModel = viewModel(factory = factory)
    val authState by viewModelAuth.authState.observeAsState()

    val showError by viewModelAuth.showError.observeAsState(false)
    val email by viewModelAuth.email.observeAsState("")
    val password by viewModelAuth.password.observeAsState("")

    if (authState == AuthState.Authenticated) {
        navToHome()
    } else {
        if (showError) {
            val errorMessage = (authState as? AuthState.Error)?.message
            if (errorMessage?.contains("invalid_credentials") == true) {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show()
            }
            viewModelAuth.errorMessageShowed()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Create Account")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { viewModelAuth.editEmail(it) },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModelAuth.editPassword(it) },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModelAuth.signUp() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
        }
    }
}
