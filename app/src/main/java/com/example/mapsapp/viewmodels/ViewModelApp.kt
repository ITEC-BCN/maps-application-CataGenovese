package com.example.mapsapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelApp: ViewModel() {

    val database = MyApp.database

    //nom del lloc on farem la foto
    private val _namePlace= MutableLiveData<String>()
    val namePlace= _namePlace

    //descripcio
    private val _description = MutableLiveData<String>()
    val description = _description

    //funcio per modificar el nom


    //coordenades
    private val coordenades=0

    /*   === SETTERS ===   */

    fun setName(name:String) {
        _namePlace.value= name
    }

    fun setDescription(novaDescription: String) {
        _description.value= novaDescription
    }

}