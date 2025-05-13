import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.ViewModelApp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.mapsapp.ui.screens.CameraScreen
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MarkerDetail(vM: ViewModelApp, id: Int, navigateBack: () -> Unit) {
    val actualMarker by vM.actualMarker.observeAsState()
    val markerName by vM.namePlace.observeAsState("")
    val markerDescription by vM.description.observeAsState("")
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by vM.missatgeAvis.observeAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Log.d("Snackbar", "Mostrando mensaje: $it")
            snackbarHostState.showSnackbar(it)
            vM.setAvis()
        }
    }

    if (actualMarker == null) {
        vM.getMarker(id)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (actualMarker != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //Edit Marker
                Text(
                    text = "Edit Marker",
                    fontSize = 24.sp,
                    color = Color(0xFF4A4A4A),
                    modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                )

                TextField(
                    value = markerName,
                    onValueChange = { vM.setName(it) },
                    label = { Text("New Name") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                TextField(
                    value = markerDescription,
                    onValueChange = { vM.setDescription(it) },
                    label = { Text("New Description") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                // Sección de imagen (solo si existe)

                actualMarker?.foto?.let { fotoUrl ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = fotoUrl,
                            contentDescription = "Marker image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(160.dp)
                                .background(Color(0xFFEFE9E1), RoundedCornerShape(16.dp))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            CameraScreen1(vM, onImageCaptured = { uri ->
                                selectedImageUri = uri
                            })

                            Button(
                                onClick = { /* delete button */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFBE0000)
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.width(140.dp)
                            ) {
                                Text("Eliminar img")
                            }
                        }
                    }
                }


                Button(
                    onClick = {
                        if (markerName.isBlank() || markerDescription.isBlank()) {
                            vM.setAvisCreate(
                                when {
                                    markerName.isBlank() && markerDescription.isBlank() -> "⚠️ Name and description required!"
                                    markerName.isBlank() -> "⚠️ Please enter a name!"
                                    else -> "⚠️ Please enter a description!"
                                }
                            )
                        } else {
                            vM.updateMarkerInfo(
                                markerName,
                                markerDescription
                            )

                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2B48C)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Update")
                }


                Button(
                    onClick = { navigateBack() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBFAF9B)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Turn back")
                }
            }

        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        // Snackbar visible encima del contenido
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            snackbar = { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    contentColor = Color.White
                )
            }
        )
    }
}


@Composable
fun CameraScreen1(viewModelApp: ViewModelApp, onImageCaptured: (Uri) -> Unit) {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val bitmap by viewModelApp.bitmap.observeAsState(null)

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri.value != null) {
                onImageCaptured(imageUri.value!!)
                val stream = context.contentResolver.openInputStream(imageUri.value!!)
                //guardem imatge
                viewModelApp.setBitmap(BitmapFactory.decodeStream(stream))
            }
        }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri.value = it
                onImageCaptured(it)
                val stream = context.contentResolver.openInputStream(it)
                viewModelApp.setBitmap(BitmapFactory.decodeStream(stream))
            }
        }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Selecciona una opción") },
            text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val uri = createImageUri(context)
                    imageUri.value = uri
                    takePictureLauncher.launch(uri!!)
                }) {
                    Text("Tomar Foto")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    pickImageLauncher.launch("image/*")
                }) {
                    Text("Elegir de Galería")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD2B48C)
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.width(140.dp)
        ) {
            Text("Cambiar img")
        }

        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
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

