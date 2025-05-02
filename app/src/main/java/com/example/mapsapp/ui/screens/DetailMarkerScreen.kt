import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment


@Composable
fun MarkerDetail(id: Int, navigateBack: () -> Unit) {
    val vM = viewModel<ViewModelApp>()
    val actualMarker by vM.actualMarker.observeAsState()
    val markerName by vM.namePlace.observeAsState("")
    val markerDescription by vM.description.observeAsState("")
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by vM.missatgeAvis.observeAsState()

    //sirve para mostrar un Snackbar automáticamente cuando cambie el valor de errorMessage
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            vM.setAvis()
        }
    }

    if (actualMarker == null) {
        vM.getMarker(id)
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = Color(0xFFFFF8F1) // Beige suave
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (actualMarker != null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
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

                        actualMarker?.foto?.let { fotoUrl ->
                            AsyncImage(
                                model = fotoUrl,
                                contentDescription = "Marker image",
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .size(220.dp)
                                    .background(Color(0xFFEFE9E1), RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))


                        Button(
                            onClick = {
                                vM.setAvis()
                                vM.updateMarker(
                                    id, markerName, markerDescription,
                                    actualMarker!!.lat, actualMarker!!.long, actualMarker!!.foto
                                )
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
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomEnd) ,
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        contentColor = Color.White,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            )
        }
    }
}

