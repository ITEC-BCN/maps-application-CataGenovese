package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.mapsapp.viewmodels.ViewModelApp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MarkerScreen(lat: Double, long: Double, viewModelApp: ViewModelApp) {
    val name by viewModelApp.namePlace.observeAsState("")
    val description by viewModelApp.description.observeAsState("")
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModelApp.missatgeAvis.observeAsState()
    val bitmap by viewModelApp.bitmap.observeAsState()
    val markerCreated by viewModelApp.markerCreated.observeAsState(false)


    // show Snackbar when there's an error message
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it) // Show Snackbar
            viewModelApp.setAvis() // Clear message after showing
        }
    }

    LaunchedEffect(markerCreated) {
        if (markerCreated) {
            selectedImageUri = null
            viewModelApp.setMarkerCreated(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top= 40.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { viewModelApp.setName(it) },
            label = { Text(text = "Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TextField(
            value = description,
            onValueChange = { viewModelApp.setDescription(it) },
            label = { Text(text = "Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp)
        )

        // Camera Component
        CameraScreen(viewModelApp, onImageCaptured = { uri ->
            selectedImageUri = uri
        })

        Button(
            onClick = {
                if (name.isBlank() || description.isBlank()) {
                    viewModelApp.setAvisCreate(
                        when {
                            name.isBlank() && description.isBlank() -> "⚠️ Name and description required!"
                            name.isBlank() -> "⚠️ Please enter a name!"
                            else -> "⚠️ Please enter a description!"
                        }
                    )
                } else {
                    viewModelApp.createNewMarker(
                        name,
                        description,
                        lat,
                        long,
                        foto = bitmap // Puede ser null
                    )
                    viewModelApp.setAvisCreate("Marker creado")
                    Log.d("cata1", "marker: $")
                }
            }
            , modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBCA577),
                contentColor = Color(0xFFFFF5E1)
            ),
            shape = RoundedCornerShape(8.dp)
        )  {
            Text("Create Marker")
        }

        // Snackbar Host (for notifications)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(top = 16.dp),
            snackbar = { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }
}


@Composable
fun CameraScreen(viewModelApp: ViewModelApp, onImageCaptured: (Uri) -> Unit) {
    val context = LocalContext.current
    val imagenURI = remember { mutableStateOf<Uri?>(null) }
    val bitmap by viewModelApp.bitmap.observeAsState(null)

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imagenURI.value != null) {
            onImageCaptured(imagenURI.value!!)

            val stream = context.contentResolver.openInputStream(imagenURI.value!!)
            stream?.use {
                val originalBitmap = BitmapFactory.decodeStream(it)
                val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
                val newWidth = 800
                val newHeight = (newWidth / aspectRatio).toInt()
                val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
                viewModelApp.setBitmap(resizedBitmap)
            } ?: run {
                Log.e("TakePicture", "Error al abrir InputStream para la URI de la imagen.")
            }
        } else {
            Log.e("TakePicture", "La imagen no fue tomada o la URI es nula.")
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            onImageCaptured(uri)

            val stream = context.contentResolver.openInputStream(uri)
            stream?.use {
                val originalBitmap = BitmapFactory.decodeStream(it)
                val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
                val newWidth = 800
                val newHeight = (newWidth / aspectRatio).toInt()
                val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
                viewModelApp.setBitmap(resizedBitmap)
            } ?: run {
                Log.e("PickImage", "Error al abrir InputStream para la imagen seleccionada.")
            }
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Selecciona una opción") },
            text = { Text("¿Quieres tomar una foto o seleccionar de la galería?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val uri = createImageUri(context)
                    imagenURI.value = uri
                    cameraLauncher.launch(uri!!)
                }) {
                    Text("Tomar Foto")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    pickImageLauncher.launch("image/*")
                }) {
                    Text("Galería")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBCA577),
                contentColor = Color(0xFFFFF5E1)
            ),
            shape = RoundedCornerShape(8.dp)
        )  {
            Text("Seleccionar Imagen")
        }


        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}


fun createImageUri(context: Context): Uri? {
    val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}