package com.example.mapsapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.data.Marker_bdd
import com.example.mapsapp.ui.navigation.MySupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelApp : ViewModel() {

    val database = MyApp.database

    //nom del lloc on farem la foto
    private val _namePlace = MutableLiveData<String>()
    val namePlace = _namePlace

    //descripcio
    private val _description = MutableLiveData<String>()
    val description = _description

    //markerActual
    private var _actualMarker: Marker_bdd? = null

    //lista marcadores
    private val _markersList = MutableLiveData<List<Marker_bdd>>()
    val markerList = _markersList

    //coordenades
    private val coordenades = MutableLiveData<Long>()


    /*========SETTERS========*/

    fun setName(name: String) {
        _namePlace.value = name
    }

    fun setDescription(novaDescription: String) {
        _description.value = novaDescription
    }

    /*CRUD*/

    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val databaseMarkers = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = databaseMarkers
            }
        }
    }

    fun insertNewMarker(name: String, description: String, coordenades: String, foto: String) {
        val newMarker = Marker_bdd(
            id = 0,
            name = name,
            description = description,
            coordenades = coordenades,
            foto = foto
        )
        CoroutineScope(Dispatchers.IO).launch {
            database.insertMarker(newMarker)
            getAllMarkers()
        }
    }

    fun updateMarker(
        id: Int,
        name: String,
        description: String,
        coordenades: String,
        foto: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarker(id, name, description, coordenades, foto)
            getAllMarkers()
        }
    }

    fun deleteMarker(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteMarker(id.toString())
            getAllMarkers()
        }
    }

    fun getMarker(id: Int) {
        if (_actualMarker == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val marker = database.getMarker(id)
                withContext(Dispatchers.Main) {
                    _actualMarker = marker
                    _namePlace.value = marker.name
                    _description.value = marker.description
                }
            }
        }
    }

}