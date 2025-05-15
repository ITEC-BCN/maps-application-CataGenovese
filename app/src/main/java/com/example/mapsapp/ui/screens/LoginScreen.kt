package com.example.mapsapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.R
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.ViewModelFactory

@Composable
fun Login(
    navToRegister: () -> Unit,
    navToHome: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(factory = ViewModelFactory(SharedPreferencesHelper(context)))

    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val authState by viewModel.authState.observeAsState()
    val showError by viewModel.showError.observeAsState(false)

    if (authState == AuthState.Authenticated) {
        navToHome()
    } else {
        if (showError) {
            val errorMessage = (authState as? AuthState.Error)?.message
            val message = when {
                errorMessage?.contains("invalid_credentials") == true -> "Credenciales incorrectas"
                else -> "Ha ocurrido un error"
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.errorMessageShowed()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFF5F5DC), Color(0xFFECECEC))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.maps), // Reemplaza con tu recurso de icono
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(72.dp)
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "Welcome Back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.editEmail(it) },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.editPassword(it) },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            viewModel.signIn()
                        } else {
                            Toast.makeText(context, "Email y contraseña requeridos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B))
                ) {
                    Text("Login", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = navToRegister) {
                    Text(
                        text = "Don’t have an account? Register",
                        color = Color(0xFF607D8B)
                    )
                }
            }
        }
    }
}
