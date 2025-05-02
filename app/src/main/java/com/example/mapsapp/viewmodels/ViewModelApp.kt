package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Marker_bdd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    /*========SETTERS========*/

    fun setName(name: String) {
        _namePlace.value = name
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

    //actualitzar un marker
    fun updateMarker(
        id: Int,
        name: String,
        description: String,
        lat: Double,
        long: Double,
        foto: String
    ) {
        if (name.isBlank() || description.isBlank()) {
            _missatgeAvis.value = when {
                name.isBlank() && description.isBlank() -> "Introdueix un nom i una descripció"
                name.isBlank() -> "Introdueix un nom "
                else -> "Introdueix una descripció"
            }
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



}


