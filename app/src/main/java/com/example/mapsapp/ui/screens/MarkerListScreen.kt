import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Marker
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.mapsapp.viewmodels.ViewModelApp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.mapsapp.R


//funcio llista de markers
@Composable
fun ListMarkers(vM: ViewModelApp, navigateToDetail: (Int) -> Unit) {
    val markerList by vM.markerList.observeAsState(emptyList<Marker>())

    vM.getAllMarkers()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.map2),
            contentDescription = "wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 147.dp)
        ) {

            Text(
                text = "Markers List",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color.DarkGray
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            {
                itemsIndexed(markerList, key = {_, marker -> marker.id }) { _, marker ->
                    val dissmissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) {
                                vM.deleteMarker(marker.id)
                                true
                            } else {
                                false
                            }
                        }
                    )
                    SwipeToDismissBox(state = dissmissState, backgroundContent = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Red)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier= Modifier.padding(bottom = 15.dp)
                            )
                        }
                    }) {
                        MarkerItem(marker) {
                            navigateToDetail(marker.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarkerItem(marker: Marker, navigateToDetail: (Int) -> Unit) {
    val imagePainter = if (!marker.foto.isNullOrEmpty()) {
        rememberAsyncImagePainter(model = marker.foto)
    } else {
        painterResource(id = R.drawable.maps)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFDFCF8))
            .clickable { navigateToDetail(marker.id) }
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "Marker image",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = marker.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
