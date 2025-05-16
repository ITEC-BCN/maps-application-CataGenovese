package com.example.mapsapp.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mapsapp.BuildConfig
import com.example.mapsapp.utils.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SupabaseManager {
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY
    lateinit var client: SupabaseClient
    lateinit var storage: Storage

    constructor() {
        client = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
            install(Postgrest)
            install(Storage)
            install(Auth) {
                autoLoadFromStorage = true
            }
        }
        storage = client.storage
    }

    suspend fun signUpWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }

    suspend fun signInWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }
    fun retrieveCurrentSession(): UserSession?{
        val session = client.auth.currentSessionOrNull()
        return session
    }

    fun refreshSession(): AuthState {
        try {
            client.auth.currentSessionOrNull()
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateMarker(
        id: Int,
        name: String,
        description: String,
        imageName: String?,
        imgFile: ByteArray?
    ) {
        if (imageName != null && imgFile != null) {
            Log.d("Cata", "comencem actualitzant l'img ")
            storage.from("images").delete(imageName)
            Log.d("Cata", "esborrem imgName $imageName")
            val newImageName = uploadImage(imgFile)
            Log.d("Cata", "new img $newImageName")
            Log.d("Cata", "nova img bytearrray")
            client.from("Map").update({
                set("name", name)
                set("description", description)
                set("foto", newImageName)
            }) { filter { eq("id", id) } }
            Log.d("Cata", "id $id")
        }
        else {
            Log.d("Cata", "se me va por otro lado ")
            client.from("Map").update({
                set("name", name)
                set("description", description)
            }) { filter { eq("id", id) } }
        }

    }
    suspend fun deleteMarkerImageOnly(id: Int, imageUrl: String) {
        try {
            // 1. Eliminar la imagen del almacenamiento
            val imageName = imageUrl.removePrefix("$supabaseUrl/storage/v1/object/public/images/")
            storage.from("images").delete(imageName)

            // 2. Establecer el campo `foto` en null en la tabla `Map`
            client.from("Map").update(
                mapOf("foto" to null)
            ) {
                filter { eq("id", id) }
            }

            Log.d("deleteImage", "Imagen eliminada correctamente")
        } catch (e: Exception) {
            Log.e("deleteImage", "Error al eliminar imagen: ${e.localizedMessage}")
            throw e
        }
    }

//    suspend fun deleteImage(imageName: String){
//        val imgName = imageName.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
//        client.storage.from("images").delete(imgName)
//    }

//    suspend fun deleteMarkerImageOnly(id: Int, imageUrl: String) {
//        // 1. Eliminar del storage
//        deleteImage(imageUrl)
//
//        // 2. Limpiar el campo foto en la tabla
//        client.from("Map").update(
//            mapOf("foto" to null)
//        ) {
//            filter { eq("id", id) }
//        }
//    }
//
//    //funcio per substituir una imatge
//    @RequiresApi(Build.VERSION_CODES.O)
//    suspend fun replaceMarkerImage(id: Int, newImage: ByteArray, oldImageUrl: String?): String {
//        // Eliminar imagen vieja si existe
//        oldImageUrl?.let { deleteImage(it) }
//
//        // Subir nueva imagen
//        val newImageUrl = uploadImage(newImage)
//
//        // Actualizar referencia en la base de datos
//        client.from("Map").update(
//            mapOf("foto" to newImageUrl)
//        ) {
//            filter { eq("id", id) }
//        }
//
//        return newImageUrl
//    }
}

