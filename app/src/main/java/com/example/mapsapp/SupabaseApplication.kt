package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient
import com.example.mapsapp.data.SupabaseManager
import io.github.jan.supabase.SupabaseClient

class SupabaseApplication: Application() {
    companion object{
        lateinit var supabaseAuth: SupabaseManager
        lateinit var database: MySupabaseClient
    }
    override fun onCreate() {
        super.onCreate()
        supabaseAuth = SupabaseManager()
        database = MySupabaseClient()
    }
}