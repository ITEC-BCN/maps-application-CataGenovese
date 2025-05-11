package com.example.mapsapp.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mapsapp.BuildConfig
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MySupabaseClient {
    lateinit var client: SupabaseClient
    lateinit var storage: Storage
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY

    constructor() {
        client = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
            install(Postgrest)
            install(Storage)
        }
        storage = client.storage
    }

    //SQL operations
    suspend fun getAllMarkers(): List<Marker> {
        return client.from("Map").select().decodeList<Marker>()
    }

    suspend fun getMarker(id: Int): Marker {
        return client.from("Map").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marker>()
    }

    suspend fun insertMarker(marker: Marker) {
        client.from("Map").insert(marker)
    }

    suspend fun deleteMarker(id: String) {
        client.from("Map").delete {
            filter {
                eq("id", id)
            }
        }
    }

//    // Función mejorada para eliminar marcador con imagen
//    suspend fun deleteMarkerWithImage(id: String, imageUrl: String?) {
//        // Eliminar imagen primero si existe
//        imageUrl?.let { deleteImage(it) }
//
//        // Luego eliminar el marcador
//        deleteMarker(id)
//    }
//
//    // Función para eliminar solo la imagen del marcador (versión alternativa)
//    suspend fun clearMarkerImage(id: Int, imageUrl: String) {
//        deleteImage(imageUrl)
//
//        // Usando el enfoque de mapa en lugar de set()
//        client.from("Map").update(
//            mapOf("foto" to null)
//        ) {
//            filter { eq("id", id) }
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imageName = storage.from("images")
            .upload(path = "image_${fechaHoraActual.format(formato)}.png", data = imageFile)
        Log.d("cata", "imageName: $imageName")
        Log.d("cata", "buildImageURL: ${buildImageUrl(imageFileName = imageName.path)}")
        return buildImageUrl(imageFileName = imageName.path)

    }

    fun buildImageUrl(imageFileName: String) =
        "${this.supabaseUrl}/storage/v1/object/public/images/${imageFileName}"


//    suspend fun updateMarkerInfo(id: Int, name: String, description: String) {
//        client.from("Marker").update({
//            set("name", name)
//            set("description", description)
//        }) {
//            filter { eq("id", id) }
//        }
//    }

    suspend fun updateMarker(
        id: Int,
        name: String,
        description: String,
        lat: Double,
        long: Double,
        imageName: String? = null,  // Hacer opcional
        foto: ByteArray? = null     // Hacer opcional
    ) {
        if (imageName != null && foto != null) {
            storage.from("images").update(path = imageName, data = foto)
        }

        client.from("Map").update({
            set("name", name)
            set("description", description)
            set("lat", lat)
            set("long", long)
            if (imageName != null) {
                set("foto", buildImageUrl(imageFileName = imageName))
            }
        }) {
            filter { eq("id", id) }
        }
    }
}

