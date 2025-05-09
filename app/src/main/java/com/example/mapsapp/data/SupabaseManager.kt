//package com.example.mapsapp.data
//
//import android.provider.ContactsContract.CommonDataKinds.Email
//import com.example.mapsapp.utils.AuthResponse
//import com.russhwolf.settings.BuildConfig
//import io.github.jan.supabase.auth.Auth
//import io.github.jan.supabase.auth.auth
//import io.github.jan.supabase.createSupabaseClient
//
//class SupabaseManager {
//    private val supabaseUrl = BuildConfig.SUPABASE_URL
//    private val supabaseKey = BuildConfig.SUPABASE_KEY
//    private val supabase = createSupabaseClient(
//        supabaseUrl = supabaseUrl,
//        supabaseKey = supabaseKey
//    ) {
//        install(Auth) {
//            autoLoadFromStorage = true
//        }
//    }
//
//    suspend fun signUpWithEmail(emailValue: String, passwordValue: String): AuthResponse {
//        try {
//            supabase.auth.signUpWith(Email) {
//                email = emailValue
//                password = passwordValue
//            }
//            return AuthResponse.Success
//        } catch (e: Exception) {
//            return AuthResponse.Error(e.localizedMessage)
//        }
//    }
//
//}
