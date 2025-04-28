package com.example.mapsapp.ui.navigation

import com.example.mapsapp.data.Marker_bdd
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

class MySupabaseClient() {
    lateinit var client: SupabaseClient
    constructor(supabaseUrl: String, supabaseKey: String): this(){
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
        }
    }
    //SQL operations
    suspend fun getAllMarkers(): List<Marker_bdd> {
        return client.from("Map").select().decodeList<Marker_bdd>()
    }


}
