package com.example.mapsapp.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Marker_bdd
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class ViewModelApp : ViewModel() {

    val database = MyApp.database

    //nom del lloc on farem la foto
    private val _namePlace = MutableLiveData<String>()
    val namePlace = _namePlace

    //descripcio
    private val _description = MutableLiveData<String>()
    val description = _description

    //val img
    private val _img = MutableLiveData<String>()
    val img = _img

    //markerActual
    private var _actualMarker = MutableLiveData<Marker_bdd>()
    val actualMarker = _actualMarker

    //lista marcadores
    private val _markersList = MutableLiveData<List<Marker_bdd>>()
    val markerList = _markersList

    private val _missatgeAvis = MutableLiveData<String?>()
    val missatgeAvis = _missatgeAvis

    //expanded menu
    private val _expanded= MutableLiveData<Boolean>(true)
    val expanded= _expanded

    //imatge
    var selectedImageUri = mutableStateOf<Uri?>(null)
        private set

    // El estado del snackbar
    val snackbarHostState = SnackbarHostState()

    //searcchbar
    private val _searchBar= MutableLiveData("")
    val searchBar: LiveData<String> = _searchBar

    //tipos de mapa
    private val _tipusMapa= MutableLiveData<String>()
    val tipusMapa= _tipusMapa

    private val _selectedItem = MutableLiveData<Int>(0)
    val selectedItem= _selectedItem

    /*========SETTERS========*/

    fun setSelectedItem(n: Int) {
        _selectedItem.value= n
    }

    fun setName(name: String) {
        _namePlace.value = name
    }

    fun setSearchText(text: String) {
        _searchBar.value = text
    }

    fun setExpanded(expanded: Boolean) {
        _expanded.value = expanded
    }

    //fem un c
    fun setAvis() {
        _missatgeAvis.value = null
    }

    fun setAvisCreate(string: String) {
        _missatgeAvis.value = string // Set error message

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000) // Allow the message to stay visible for 2 seconds
            _missatgeAvis.value = null
        }
    }

    fun setDescription(novaDescription: String) {
        _description.value = novaDescription
    }

    fun setMapa(mapa: String) {
        _tipusMapa.value = mapa
    }
//    fun updateSelectedImageUri(uri: Uri?) {
//        selectedImageUri.value = uri
//    }

    /*CRUD*/

    //obtenir tots els markers
    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val databaseMarkers = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = databaseMarkers
            }
        }
    }

    //crear un marker
    fun insertNewMarker(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        foto: String
    ) {
        // condicio per verificar si el nom o la descripcio estna buits
        if (name.isBlank() || description.isBlank()) {
            val missatge = when {
                name.isBlank() && description.isBlank() -> "⚠\uFE0F Name and description required!"
                name.isBlank() -> "⚠\uFE0F Please enter a name!"
                else -> "⚠\uFE0F Please enter a description!"
            }
            //mostrem missatge
            _missatgeAvis.value = missatge
        } else {
            //si tenim nom i descripció creem el marker amb les dades uqe ha insertat l'usuari
            val newMarker = Marker_bdd(
                id = 0,
                name = name,
                description = description,
                lat = lat,
                long = long,
                foto = foto
            )

            CoroutineScope(Dispatchers.IO).launch {
                database.insertMarker(newMarker)
                getAllMarkers()
            }
        }
    }

    //actualitzar un marker
    fun updateMarker(
        id: Int,
        name: String,
        description: String,
        lat: Double,
        long: Double,
        foto: String
    ) {
        Log.d(
            "UPDATE",
            "Called with id=$id, name=$name, description=$description, lat=$lat, long=$long, foto=$foto"
        )
        if (name.isBlank() || description.isBlank()) {
            val missatge = when {
                name.isBlank() && description.isBlank() -> "⚠\uFE0F Name and description required!"
                name.isBlank() -> "⚠\uFE0F Please enter a name!"
                else -> "⚠\uFE0F Please enter a description!"
            }
            _missatgeAvis.postValue(missatge) // Properly update error message
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                database.updateMarker(id, name, description, lat, long, foto)
                getAllMarkers()
            }
        }
    }

    //eliminar un marker
    fun deleteMarker(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteMarker(id.toString())
            getAllMarkers()
        }
    }

    //obtenir nomes un marker
    fun getMarker(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val marker = database.getMarker(id)
            withContext(Dispatchers.Main) {
                _actualMarker.value = marker
                _namePlace.value = marker.name
                _description.value = marker.description
            }
        }

    }

    //funció per fer una searchBar i buscar ubicació
    suspend fun geocodeLocation(locationName: String, apiKey: String): LatLng? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${locationName}&key=${apiKey}"
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val json = JSONObject(response.body?.string() ?: return@withContext null)
                val location = json.getJSONArray("results")
                    ?.optJSONObject(0)
                    ?.getJSONObject("geometry")
                    ?.getJSONObject("location")
                if (location != null) {
                    LatLng(location.getDouble("lat"), location.getDouble("lng"))
                } else null
            } catch (e: Exception) {
                Log.e("Geocode", "Error: ${e.message}")
                null
            }
        }
    }



}


