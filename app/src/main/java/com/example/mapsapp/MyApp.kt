package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.ui.navigation.MySupabaseClient

class MyApp: Application() {
    companion object {
        lateinit var database: MySupabaseClient
    }
    override fun onCreate() {
        super.onCreate()
        database = MySupabaseClient(
            supabaseUrl = "https://ysenmbkminqdldmhphzf.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InlzZW5tYmttaW5xZGxkbWhwaHpmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU4Mjc5MTIsImV4cCI6MjA2MTQwMzkxMn0.n8m5Prmpj5eUoAUt2pba9WnQaYbay8gQ7xF3XvHGCbg"
        )
    }
}
