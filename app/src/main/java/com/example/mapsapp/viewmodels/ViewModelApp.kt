package com.example.mapsapp.viewmodels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Marker_bdd
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

    //val img
    private val _img= MutableLiveData<String>()
    val img= _img

    //markerActual
    private var _actualMarker: Marker_bdd? = null

    //lista marcadores
    private val _markersList = MutableLiveData<List<Marker_bdd>>()
    val markerList = _markersList


    /*========SETTERS========*/

    fun setName(name: String) {
        _namePlace.value = name
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
    fun insertNewMarker(name: String, description: String, lat: Double, long:Double, foto: String) {
        val newMarker = Marker_bdd(
            id = 0,
            name = name,
            description = description,
            lat= lat,
            long= long,
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
        long:Double,
        foto: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarker(id, name, description, lat, long, foto)
            getAllMarkers()
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