package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.SupabaseApplication
import com.example.mapsapp.data.Marker
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ViewModelApp(private val sharedPreferences: SharedPreferencesHelper) : ViewModel() {

    val supabseAuth = SupabaseApplication.supabaseAuth
    val database = SupabaseApplication.supabaseAuth

    //img
    private val _bitmap = MutableLiveData<Bitmap?>(null)
    val bitmap = _bitmap

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
    private var _actualMarker = MutableLiveData<Marker>()
    val actualMarker = _actualMarker

    //lista marcadores
    private val _markersList = MutableLiveData<List<Marker>>()
    val markerList = _markersList

    private val _missatgeAvis = MutableLiveData<String?>()
    val missatgeAvis = _missatgeAvis

    //expanded menu
    private val _expanded = MutableLiveData<Boolean>(false)
    val expanded = _expanded

    //imatge
    var selectedImageUri = mutableStateOf<Uri?>(null)
        private set

    // El estado del snackbar
    val snackbarHostState = SnackbarHostState()

    //searcchbar
    private val _searchBar = MutableLiveData("")
    val searchBar: LiveData<String> = _searchBar

    //tipos de mapa
    private val _tipusMapa = MutableLiveData<String>()
    val tipusMapa = _tipusMapa

    private val _selectedItem = MutableLiveData<Int>(0)
    val selectedItem = _selectedItem

    private val _user = MutableLiveData<String?>()
    val user = _user

//    init {
//        checkExistingSession()
//    }


    /*========SETTERS========*/

    fun setBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun setSelectedItem(n: Int) {
        _selectedItem.value = n
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


//    private fun checkExistingSession() {
//        viewModelScope.launch {
//            val accessToken = sharedPreferences.getAccessToken()
//            val refreshToken = sharedPreferences.getRefreshToken()
//            when {
//                !accessToken.isNullOrEmpty() -> refreshToken()
//                !refreshToken.isNullOrEmpty() -> refreshToken()
//                else -> _authState.value = AuthState.Unauthenticated
//            }
//        }
//    }

//    fun updateSelectedImageUri(uri: Uri?) {
//        selectedImageUri.value = uri
//    }

    /*CRUD*/

    //obtenir tots els markers
    fun getAllMarkers() {
        Log.d("DANI", "Vaig a buscar markers")
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("DANI", "Comença la corrutina")
            Log.d("DANI", "URL Supabase: ${database.client.supabaseUrl}")

            val databaseMarkers = database.getAllMarkers()
            Log.d("DANI", "punt 1")
            withContext(Dispatchers.Main) {
                Log.d("DANI", "punt 2")
                _markersList.value = databaseMarkers
                Log.d("DANI", "punt 3")
            }
            Log.d("DANI", "Final de la corrutina")
        }
        Log.d("DANI", "Surto de buscar markers")
    }

    //get marker id
    fun getMarker(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val marker = database.getMarker(id)
            Log.d("cata 08", "URL marker id: ${marker.foto}")
            withContext(Dispatchers.Main) {
                _actualMarker.value = marker
                _namePlace.value = marker.name
                _description.value = marker.description
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

    //crear un marker
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNewMarker(
        name: String,
        description: String,
        lat: Double,
        long: Double,
        foto: Bitmap?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageUrl = if (foto != null) {
                    val stream = ByteArrayOutputStream()
                    foto.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    Log.d("MarkerCreation", "Uploading image")
                    database.uploadImage(stream.toByteArray())
                } else {
                    null
                }

                val newMarker = Marker(
                    name = name,
                    description = description,
                    foto = imageUrl,
                    lat = lat,
                    long = long
                )

                Log.d("MarkerCreation", "Creating marker: $newMarker")
                database.insertMarker(newMarker)
                Log.d("MarkerCreation", "Marker created successfully")

                withContext(Dispatchers.Main) {
                    // Limpiar los campos después de crear el marcador
                    _namePlace.value = ""
                    _description.value = ""
                    _bitmap.value = null
                    selectedImageUri.value = null

                    _missatgeAvis.value = "Marker creado correctamente"
                    delay(2000)
                    _missatgeAvis.value = null
                }
            } catch (e: Exception) {
                Log.e("MarkerCreation", "Error creating marker", e)
                withContext(Dispatchers.Main) {
                    _missatgeAvis.value = "Error al crear marker: ${e.message}"
                    delay(2000)
                    _missatgeAvis.value = null
                }
            }
        }
    }

    //funcio per actualitzar el marker
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMarkerInfo(name: String, description: String) {
        val currentMarker = _actualMarker.value ?: return
        val currentBitmap = _bitmap.value // Usamos el Bitmap que ya está en el ViewModel

        if (currentBitmap == null) {
            // Si no hay Bitmap, solo actualizamos nombre y descripción (sin tocar la imagen)
            CoroutineScope(Dispatchers.IO).launch {
                database.updateMarker(
                    id = currentMarker.id,
                    name = name,
                    description = description,
                    //no actualitzem img
                    imageName = null,
                    imgFile = null
                )
            }
        } else {
            // Si hay Bitmap, actualizamos img tambe
            val stream = ByteArrayOutputStream()
            currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageName = currentMarker.foto?.removePrefix("https://ysenmbkminqdldmhphzf.supabase.co/storage/v1/object/public/images/")

            CoroutineScope(Dispatchers.IO).launch {
                database.updateMarker(
                    id = currentMarker.id,
                    name = name,
                    description = description,
                    imageName = imageName.toString(),
                    imgFile = stream.toByteArray()
                )
            }
        }
    }


    //funció per veure si hi ha dades d’usuari a SharedPreferences:
//    private fun checkExistingSession() {
//        viewModelScope.launch {
//            val accessToken = sharedPre.getAccessToken()
//            val refreshToken = sharedPreferences.getRefreshToken()
//            when {
//                !accessToken.isNullOrEmpty() -> refreshToken()
//                !refreshToken.isNullOrEmpty() -> refreshToken()
//                else -> _authState.value = AuthState.Unauthenticated
//            }
//        }
//    }





    //    //actualitzar un marker
//    fun updateMarker(
//        id: Int,
//        name: String,
//        description: String,
//        lat: Double,
//        long: Double,
//        foto: Bitmap?
//    ) {
//        val stream = ByteArrayOutputStream()
//        foto?.compress(Bitmap.CompressFormat.PNG, 0, stream)
//        val imageName =
//            _actualMarker.value?.foto?.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
//        CoroutineScope(Dispatchers.IO).launch {
//            database.updateMarker(
//                id,
//                name,
//                description,
//                lat,
//                long,
//                imageName.toString(),
//                stream.toByteArray()
//            )
//        }
//    }

//    fun deleteImage(id: Int, imageUrl: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                database.clearMarkerImage(id, imageUrl)
//                withContext(Dispatchers.Main) {
//                    // Actualizar el estado
//                    _actualMarker.value = _actualMarker.value?.copy(foto = null)
//                    _bitmap.value = null
//                    _missatgeAvis.value = "Imagen eliminada correctamente"
//                    delay(2000)
//                    _missatgeAvis.value = null
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _missatgeAvis.value = "Error al eliminar imagen: ${e.message}"
//                    delay(2000)
//                    _missatgeAvis.value = null
//                }
//            }
//        }
//    }
//    //funció per fer una searchBar i buscar ubicació
//    suspend fun geocodeLocation(locationName: String, apiKey: String): LatLng? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${locationName}&key=${apiKey}"
//                val client = OkHttpClient()
//                val request = Request.Builder().url(url).build()
//                val response = client.newCall(request).execute()
//                val json = JSONObject(response.body?.string() ?: return@withContext null)
//                val location = json.getJSONArray("results")
//                    ?.optJSONObject(0)
//                    ?.getJSONObject("geometry")
//                    ?.getJSONObject("location")
//                if (location != null) {
//                    LatLng(location.getDouble("lat"), location.getDouble("lng"))
//                } else null
//            } catch (e: Exception) {
//                Log.e("Geocode", "Error: ${e.message}")
//                null
//            }
//        }
//    }

}