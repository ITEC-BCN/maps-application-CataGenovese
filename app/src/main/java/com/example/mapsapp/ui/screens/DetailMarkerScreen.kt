import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.ViewModelApp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MarkerDetail(id: Int, navigateBack: () -> Unit) {
    val vM = viewModel<ViewModelApp>()
    val actualMarker by vM.actualMarker.observeAsState()

    val markerName: String by vM.namePlace.observeAsState("")
    val markerDescription: String by vM.description.observeAsState("")
    //si te foto
    val fotoUrl = actualMarker?.foto

    if(actualMarker==null) {
        vM.getMarker(id)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(actualMarker != null){
            TextField(
                value = markerName,
                onValueChange = { vM.setName(it) },
                label = { Text(text = "New Name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // TextField para la descripción
            TextField(
                value = markerDescription,
                onValueChange = { vM.setDescription(it) },
                label = { Text(text = "New Description") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )


            // Mostrar la imagen
            actualMarker?.foto?.let { fotoUrl ->
                AsyncImage(
                    model = fotoUrl,
                    contentDescription = "Marker image",
                    modifier = Modifier.size(200.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }

            Button( onClick = {
                vM.updateMarker(id, markerName, markerDescription, actualMarker!!.lat, actualMarker!!.long, actualMarker!!.foto)
            } ) {
                Text("Update")
            }
            Button(onClick = {
                navigateBack()
            }) {
                Text("Turn back")
            }
        }
        else{
            CircularProgressIndicator()
        }
    }
}