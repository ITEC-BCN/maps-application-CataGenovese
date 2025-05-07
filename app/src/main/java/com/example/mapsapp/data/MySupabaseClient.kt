package com.example.mapsapp.ui.navigation

import android.content.Context
import android.net.Uri
import com.example.mapsapp.data.Marker_bdd
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload


class MySupabaseClient() {
    lateinit var client: SupabaseClient

    constructor(supabaseUrl: String, supabaseKey: String) : this() {
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
            install(Storage)

        }
    }

    //SQL operations
    suspend fun getAllMarkers(): List<Marker_bdd> {
        return client.from("Map").select().decodeList<Marker_bdd>()
    }

    suspend fun getMarker(id: Int): Marker_bdd {
        return client.from("Map").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marker_bdd>()
    }

    suspend fun insertMarker(marker: Marker_bdd) {
        client.from("Map").insert(marker)
    }

    suspend fun updateMarker(
        id: Int,
        name: String,
        description: String,
        lat: Double,
        long: Double,
        foto: String,
    ) {
        client.from("Map").update({
            set("name", name)
            set("description", description)
            set("lat", lat)
            set("long", long)
            set("foto", foto)
        }) {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun deleteMarker(id: String) {
        client.from("Map").delete {
            filter {
                eq("id", id)
            }
        }
    }
}
